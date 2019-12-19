package fanrong.cwvwalled.parenter

import android.webkit.ValueCallback
import com.facebook.common.util.Hex
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.*
import fanrong.cwvwalled.http.model.response.QueryNodeGasResp
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.utils.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.XcJsonUtils
import java.lang.RuntimeException
import java.math.BigDecimal

class TransferEthPresenter : TransferPresenter {

    override fun getGasPrice(callBack: ValueCallBack<String?>) {

        val queryNodeGasReq = QueryNodeGasReq()
        queryNodeGasReq.node_url = GreNodeOperator.queryETHnode().node_url
        queryNodeGasReq.dapp_id = Constants.DAPP_ID

        RetrofitClient.getETHNetWorkApi()
                .queryNodeGas(ConvertToBody.ConvertToBody(queryNodeGasReq))
                .enqueue(object : Callback<QueryNodeGasResp> {
                    override fun onResponse(call: Call<QueryNodeGasResp>, response: Response<QueryNodeGasResp>) {
                        val body = response.body()
                        if (body != null && "1".equals(body.err_code)) {
                            callBack.valueBack(body!!.gas_price)
                        }
                    }

                    override fun onFailure(call: Call<QueryNodeGasResp>, t: Throwable) {

                    }
                })
    }

    constructor(coinBeanModel: LiteCoinBeanModel) : super(coinBeanModel)


    var signedMessage = ""
    lateinit var transferReq: TransferReq
    lateinit var callBack: TransferResultCallBack

    override fun transferMoney(req: TransferReq, callBack: TransferResultCallBack) {
        transferReq = req
        this.callBack = callBack

        // 公共参数赋值
        if (CheckedUtils.nonEmpty(req.ex_data)) {
            try {
                val jsonObject = JSONObject(req.ex_data)
                req.ex_data = XcJsonUtils.getString(jsonObject, "note")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        if (CheckedUtils.isEmpty(req.ex_data)) {
            req.ex_data = ""
        }


        val signParamter = CallJsCodeUtils.EthParamter()
        signParamter.chainId = Constants.chain_Id

        signParamter.nonce = "0x" + Integer.toHexString(Integer.valueOf(req.nonce))

        if (MoneyUtils.isInteger(req.gas_price)) {
            signParamter.gasPrice = "0x" + java.lang.Long.toHexString(java.lang.Long.parseLong(
                    BigDecimal(req.gas_price).toBigInteger().toString()))
        } else {
            signParamter.gasPrice = "0x" + java.lang.Double.toHexString(java.lang.Double.parseDouble(req.gas_price))
        }

        if (isEth()) {
            signParamter.to = req.to_addr
            signParamter.value = "0x" + MoneyUtils.toHexValue(MoneyUtils.getMultiplyEighteen(req.value)).toUpperCase()
            signParamter.data = "0x0"
        } else {
            signParamter.to = coinBeanModel.contract_addr
            signParamter.value = "0x0"

            val upperCase = MoneyUtils.toHexValue(MoneyUtils.getMultiplyEighteen(req.value)).toUpperCase()
            val valueSize = "0000000000000000000000000000000000000000000000000000000000000000"
            signParamter.data = "0xa9059cbb000000000000000000000000" +
                    req.to_addr.replaceFirst("0x", "") +
                    // 截掉 金额的长度 然后拼接上金额
                    valueSize.substring(0, valueSize.length - upperCase.length) + upperCase
        }


        SWLog.e(signParamter)
        var privateKey = ""
        val withAddress = GreWalletOperator.queryWalletWithAddress(coinBeanModel.sourceAddr)
        if ("ETH".equals(withAddress.walletType)) {
            privateKey = withAddress.privateKey!!
        } else {
            throw RuntimeException("没有对应的钱包地址")
        }
        CallJsCodeUtils.eth_ecHexSign(signParamter, privateKey, ValueCallback<String> { value ->
            signedMessage = CallJsCodeUtils.readStringJsValue(value)
            SWLog.e(value)
            if (isEth()) {
                transfer()
            } else {
                daiTransfer()
            }
        })


    }


    /**
     * 真实请求转账处理
     * ETH 主币接口
     */
    private fun transfer() {
        transferReq.dapp_id = Constants.DAPP_ID
        transferReq.symbol = AppUtils.getRealSymbol(coinBeanModel.coin_symbol)
        transferReq.node_url = GreNodeOperator.queryETHnode().node_url
        transferReq.contract_addr = coinBeanModel.contract_addr
        transferReq.tx_type = "转帐"

        transferReq.gas_limit = "400000"
        // 页面信息如下
        //        transferReq.gas_price = "";  页面进行赋值
        //        req.nonce = walletNonce;
        //        req.to_addr = address;
        //        req.value = money;   没用乘 18个零
        //        req.gas_price = gas_price;

        transferReq.from_addr = coinBeanModel.sourceAddr
        transferReq.value = MoneyUtils.getMultiplyEighteen(transferReq.value)
        transferReq.signed_message = if (signedMessage.startsWith("0x")) signedMessage.replaceFirst("0x".toRegex(), "") else signedMessage
        transferReq.timestamp = System.currentTimeMillis().toString() + ""

        transferReq.ex_data = Hex.encodeHex(transferReq.ex_data.toByteArray(), false)
        RetrofitClient.getETHNetWorkApi()
                .mainTransfer(ConvertToBody.ConvertToBody(transferReq))
                .enqueue(object : Callback<WalletTransferModel> {
                    override fun onResponse(call: Call<WalletTransferModel>, response: Response<WalletTransferModel>) {
                        if ("1".equals(response.body()!!.err_code)) {
                            callBack.success()
                        } else {
                            callBack.failed(response.body()!!.msg)
                        }
                    }

                    override fun onFailure(call: Call<WalletTransferModel>, t: Throwable) {
                        callBack.failed("网络请求失败")

                    }
                })
    }

    /**
     * 真实请求转账处理
     * ETH dai 接口
     */
    private fun daiTransfer() {
        transferReq.dapp_id = Constants.DAPP_ID
        transferReq.symbol = AppUtils.getRealSymbol(coinBeanModel.coin_symbol)
        transferReq.node_url = GreNodeOperator.queryETHnode().node_url
        transferReq.contract_addr = coinBeanModel.contract_addr
        transferReq.tx_type = "转帐"
        transferReq.gas_limit = "400000"
        // 页面信息如下
        //        transferReq.gas_price = "";  页面进行赋值
        //        req.nonce = walletNonce;
        //        req.to_addr = address;
        //        req.value = money;   没用乘 18个零
        //        req.gas_price = gas_price;
        // ETH 代币要传 合约地址
        transferReq.from_addr = coinBeanModel.sourceAddr
        transferReq.value = MoneyUtils.getMultiplyEighteen(transferReq.value)
        transferReq.signed_message = if (signedMessage.startsWith("0x")) signedMessage.replaceFirst("0x".toRegex(), "") else signedMessage

        transferReq.timestamp = System.currentTimeMillis().toString() + ""
        transferReq.ex_data = Hex.encodeHex(transferReq.ex_data.toByteArray(), false)

        RetrofitClient.getETHNetWorkApi()
                .transfer(ConvertToBody.ConvertToBody(transferReq))
                .enqueue(object : Callback<WalletTransferModel> {
                    override fun onResponse(call: Call<WalletTransferModel>, response: Response<WalletTransferModel>) {
                        if ("1".equals(response.body()!!.err_code)) {
                            callBack.success()
                        } else {
                            callBack.failed(response.body()!!.msg)
                        }
                    }

                    override fun onFailure(call: Call<WalletTransferModel>, t: Throwable) {
                        callBack.failed("网络请求失败")

                    }
                })
    }


    private fun isEth(): Boolean {
        return "ETH".equals(AppUtils.getRealSymbol(coinBeanModel.coin_symbol))
    }


    override fun getNonce(callBack: ValueCallBack<String?>) {

        val walletNonceReq = WalletNonceReq()
        walletNonceReq.dapp_id = Constants.DAPP_ID
        walletNonceReq.address = coinBeanModel.sourceAddr
        walletNonceReq.node_url = GreNodeOperator.queryETHnode().node_url

        RetrofitClient.getETHNetWorkApi()
                .walletNonce(ConvertToBody.ConvertToBody(walletNonceReq))
                .enqueue(object : Callback<WalletNonceModel> {
                    override fun onResponse(call: Call<WalletNonceModel>, response: Response<WalletNonceModel>) {
                        callBack.valueBack(response.body()!!.nonce)
                    }

                    override fun onFailure(call: Call<WalletNonceModel>, t: Throwable) {}
                })

    }

    override fun getBalance(callBack: ValueCallBack<String?>) {

        val gnodeModel = GreNodeOperator.queryETHnode()

        var constractAddr = coinBeanModel.contract_addr
        if (CheckedUtils.isEmpty(constractAddr)) {
            constractAddr = ""
        }

        val req = GetBalanceReq()
        req.dapp_id = Constants.DAPP_ID
        req.node_url = gnodeModel.node_url
        req.address = coinBeanModel.sourceAddr
        req.contract_addr = constractAddr

        RetrofitClient.getETHNetWorkApi()
                .ethGetBalance(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<WalletBalanceModel> {
                    override fun onResponse(call: Call<WalletBalanceModel>, response: Response<WalletBalanceModel>) {
                        val body = response.body()
                        val rightNum = MoneyUtils.getRightNum(body!!.balance)
                        callBack.valueBack(rightNum)
                    }

                    override fun onFailure(call: Call<WalletBalanceModel>, t: Throwable) {
                        callBack.valueBack("")
                    }
                })
    }
}