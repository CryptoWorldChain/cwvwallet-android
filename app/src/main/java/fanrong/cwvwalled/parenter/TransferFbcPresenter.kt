package fanrong.cwvwalled.parenter

import android.webkit.ValueCallback
import com.facebook.common.util.Hex
import com.google.gson.Gson
import com.google.protobuf.ByteString
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.*
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.utils.*
import org.brewchain.core.util.ByteUtil
import org.fc.sdk.pbgens.BcTxInfo
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.XCJsonUtils
import java.lang.RuntimeException
import java.math.BigDecimal
import java.math.BigInteger

class TransferFbcPresenter : TransferPresenter {
    constructor(coinBeanModel: LiteCoinBeanModel) : super(coinBeanModel)


    var signedMessage = ""
    lateinit var transferReq: TransferReq
    lateinit var callBack: TransferResultCallBack
    var currentTime: Long = 0

    override fun transferMoney(req: TransferReq, callBack: TransferResultCallBack) {
        transferReq = req
        this.callBack = callBack
        currentTime = System.currentTimeMillis()
        getSignedMessage()
    }


    private fun getSignedMessage() {
        transferReq.to_addr = transferReq.to_addr.replaceFirst("0x", "")
        transferReq.value = MoneyUtils.getMultiplyEighteen(transferReq.value)
        if (CheckedUtils.isEmpty(transferReq.ex_data)) {
            transferReq.ex_data = ""
        }

        var privateKey = ""
        val withAddress = GreWalletOperator.queryWalletWithAddress(coinBeanModel.sourceAddr)
        if ("CWV".equals(withAddress.walletType)) {
            privateKey = withAddress.privateKey!!
        } else {
            throw RuntimeException("没有对应的钱包地址")
        }


        val selfAddress = transferReq.to_addr
        val money = transferReq.value
        val inputBuild = BcTxInfo.MultiTransactionInput.newBuilder()
                .setAddress(ByteString.copyFrom(Hex.hexStringToByteArray(withAddress.address.replaceFirst("0x", ""))))
                .setAmount(ByteString.copyFrom(ByteUtil.bigIntegerToBytes(BigInteger(money))))
                .setNonce(Integer.valueOf(transferReq.nonce))

        if (!"CWV".equals(AppUtils.getRealSymbol(coinBeanModel.coin_symbol))) {
            inputBuild.setToken(AppUtils.getRealSymbol(coinBeanModel.coin_symbol))
        }


        val output = BcTxInfo.MultiTransactionOutput.newBuilder()
                .setAddress(ByteString.copyFrom(Hex.hexStringToByteArray(selfAddress)))
                .setAmount(inputBuild.getAmount())
                .build()


        val build = BcTxInfo.MultiTransactionBody.newBuilder()
                .addInputs(inputBuild.build())
                .addOutputs(output)
                .setTimestamp(currentTime)
                .setExdata(ByteString.copyFrom(transferReq.ex_data.toByteArray()))

        if (!"CWV".equals(AppUtils.getRealSymbol(coinBeanModel.coin_symbol))) {
            build.setType(2)
        }


        SWLog.e("inputBuild->" + Gson().toJson(inputBuild))
        SWLog.e("output->" + Gson().toJson(output))
        SWLog.e("build->" + Gson().toJson(build))

        val tag = Hex.encodeHex(build.build().toByteArray(), false)
        // byte2HexStr(build.toByteArray());
        SWLog.e("byte2HexStr->$tag")


        CallJsCodeUtils.cwv_ecHexSign(privateKey, tag) { value ->
            signedMessage = CallJsCodeUtils.readStringJsValue(value)
            if ("CWV".equals(AppUtils.getRealSymbol(coinBeanModel.coin_symbol))) {
                transfer()
            } else {
                daiTransfer()
            }

        }

    }


    /**
     * 真实请求转账处理
     * CWV 主币接口
     */
    private fun transfer() {
        transferReq.signed_message = if (signedMessage.startsWith("0x")) signedMessage.replaceFirst("0x".toRegex(), "") else signedMessage
        transferReq.node_url = GreNodeOperator.queryCWVnode().node_url
        transferReq.dapp_id = Constants.DAPP_ID
        transferReq.from_addr = coinBeanModel.sourceAddr!!.replaceFirst("0x", "")
        transferReq.ex_data = Hex.encodeHex(transferReq.ex_data.toByteArray(), false)
        transferReq.timestamp = currentTime.toString() + ""

        RetrofitClient.getFBCNetWorkApi()
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
     * CWV dai 接口
     */
    private fun daiTransfer() {
        transferReq.signed_message = if (signedMessage.startsWith("0x")) signedMessage.replaceFirst("0x".toRegex(), "") else signedMessage
        transferReq.node_url = GreNodeOperator.queryCWVnode().node_url
        transferReq.symbol = AppUtils.getRealSymbol(coinBeanModel.coin_symbol)
        transferReq.contract_addr = AppUtils.getRealSymbol(coinBeanModel.coin_symbol)
        transferReq.dapp_id = Constants.DAPP_ID
        transferReq.from_addr = coinBeanModel.sourceAddr!!.replaceFirst("0x", "")
        transferReq.ex_data = Hex.encodeHex(transferReq.ex_data.toByteArray(), false)
        transferReq.timestamp = currentTime.toString() + ""

        RetrofitClient.getFBCNetWorkApi()
                .fbcTransfer(ConvertToBody.ConvertToBody(transferReq))
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


    override fun getNonce(callBack: ValueCallBack<String?>) {

        val walletNonceReq = WalletNonceReq()
        walletNonceReq.dapp_id = Constants.DAPP_ID
        walletNonceReq.address = coinBeanModel.sourceAddr
        walletNonceReq.node_url = GreNodeOperator.queryCWVnode().node_url

        RetrofitClient.getFBCNetWorkApi()
                .fbcWalletNonce(ConvertToBody.ConvertToBody(walletNonceReq))
                .enqueue(object : Callback<WalletNonceModel> {
                    override fun onResponse(call: Call<WalletNonceModel>, response: Response<WalletNonceModel>) {
                        callBack.valueBack(response.body()!!.nonce)
                    }

                    override fun onFailure(call: Call<WalletNonceModel>, t: Throwable) {}
                })

    }

    override fun getBalance(callBack: ValueCallBack<String?>) {

        val gnodeModel = GreNodeOperator.queryCWVnode()

        var constractAddr = coinBeanModel.contract_addr
        if (CheckedUtils.isEmpty(constractAddr)) {
            constractAddr = ""
        }

        val req = GetBalanceReq()
        req.dapp_id = Constants.DAPP_ID
        req.node_url = gnodeModel.node_url
        req.address = coinBeanModel.sourceAddr
        req.contract_addr = constractAddr

        RetrofitClient.getFBCNetWorkApi()
                .fbcGetBalance(ConvertToBody.ConvertToBody(req))
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