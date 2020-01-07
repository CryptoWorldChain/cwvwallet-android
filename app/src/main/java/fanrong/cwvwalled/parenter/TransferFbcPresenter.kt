package fanrong.cwvwalled.parenter

import android.webkit.ValueCallback
import com.facebook.common.util.Hex
import com.google.gson.Gson
import com.google.protobuf.ByteString
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.common.ThreadExecutor
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.NetTools
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.*
import fanrong.cwvwalled.http.model.sdkmodel.TransferResult
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils
import java.lang.RuntimeException
import java.math.BigInteger
import org.cwv.client.sdk.HiChain
import org.cwv.client.sdk.model.TransferInfo
import xianchao.com.basiclib.BasicLibComponant
import java.util.concurrent.ThreadPoolExecutor


class TransferFbcPresenter : TransferPresenter {

    override fun getGasPrice(callBack: ValueCallBack<String?>) {
        // DO NOTHING
    }


    constructor(coinBeanModel: LiteCoinBeanModel) : super(coinBeanModel)


    var signedMessage = ""
    lateinit var transferReq: TransferReq
    lateinit var callBack: TransferResultCallBack
    var currentTime: Long = 0

    override fun transferMoney(req: TransferReq, callBack: TransferResultCallBack) {
        transferReq = req
        this.callBack = callBack
        currentTime = System.currentTimeMillis()
//        getSignedMessage()

        if ("CWV".equals(AppUtils.getRealSymbol(coinBeanModel.coin_symbol))) {
            transfer()
        } else {
            daiTransfer()
        }


    }


    /**
     * 真实请求转账处理
     * CWV 主币接口
     */
    private fun transfer() {

        var ti = TransferInfo()
        ti.setToAddr(transferReq.to_addr)
        ti.setAmount(transferReq.value)

        var outs = arrayListOf<TransferInfo>()
        outs.add(ti)

        var privateKey = ""
        val withAddress = GreWalletOperator.queryWalletWithAddress(coinBeanModel.sourceAddr)
        if ("CWV".equals(withAddress.walletType)) {
            privateKey = withAddress.privateKey!!
        } else {
            throw RuntimeException("没有对应的钱包地址")
        }

        ThreadExecutor.execute(object : Runnable {
            override fun run() {
                var result = HiChain.transferTo(withAddress.address,
                        withAddress.privateKey, transferReq.ex_data, outs)
                val transferResult = NetTools.formatJson(result, TransferResult::class.java)

                BasicLibComponant.postMainThread {
                    if (1 == transferResult?.code) {
                        callBack.success()
                    } else {
                        callBack.failed(result)
                    }
                }
            }
        })

    }

    /**
     * 真实请求转账处理
     * CWV dai 接口
     */
    private fun daiTransfer() {

        var ti = TransferInfo()
        ti.setToAddr(transferReq.to_addr)
        ti.setTokenAmount(transferReq.value)
        ti.token = AppUtils.getRealSymbol(transferReq.symbol)

        var outs = arrayListOf<TransferInfo>()
        outs.add(ti)

        var privateKey = ""
        val withAddress = GreWalletOperator.queryWalletWithAddress(coinBeanModel.sourceAddr)
        if ("CWV".equals(withAddress.walletType)) {
            privateKey = withAddress.privateKey!!
        } else {
            throw RuntimeException("没有对应的钱包地址")
        }

        ThreadExecutor.execute(object : Runnable {
            override fun run() {
                var result = HiChain.transferTo(withAddress.address,
                        withAddress.privateKey, transferReq.ex_data, outs)
                val transferResult = NetTools.formatJson(result, TransferResult::class.java)

                BasicLibComponant.postMainThread {
                    if (1 == transferResult?.code) {
                        callBack.success()
                    } else {
                        callBack.failed(result)
                    }
                }
            }
        })
    }


    override fun getNonce(callBack: ValueCallBack<String?>) {
//
//        val walletNonceReq = WalletNonceReq()
//        walletNonceReq.dapp_id = Constants.DAPP_ID
//        walletNonceReq.address = coinBeanModel.sourceAddr
//        walletNonceReq.node_url = GreNodeOperator.queryCWVnode().node_url
//
//        RetrofitClient.getFBCNetWorkApi()
//                .fbcWalletNonce(ConvertToBody.ConvertToBody(walletNonceReq))
//                .enqueue(object : Callback<WalletNonceModel> {
//                    override fun onResponse(call: Call<WalletNonceModel>, response: Response<WalletNonceModel>) {
//                        callBack.valueBack(response.body()!!.nonce)
//                    }
//
//                    override fun onFailure(call: Call<WalletNonceModel>, t: Throwable) {}
//                })

    }
     //查询余额的接口
    override fun getBalance(callBack: ValueCallBack<String?>) {

        CallJsCodeUtils.getJsHandler().evaluateJavascript("cwv.rpc.getBalance(${coinBeanModel.sourceAddr},'')", object : ValueCallback<String> {
            override fun onReceiveValue(value: String?) {
                SWLog.e("getBalance::" + value)
            }
        })


//        val gnodeModel = GreNodeOperator.queryCWVnode()
//
//        var constractAddr = coinBeanModel.contract_addr
//        if (CheckedUtils.isEmpty(constractAddr)) {
//            constractAddr = ""
//        }
//
//        val req = GetBalanceReq()
//        req.dapp_id = Constants.DAPP_ID
//        req.node_url = gnodeModel.node_url
//        req.address = coinBeanModel.sourceAddr
//        req.contract_addr = constractAddr
//
//        RetrofitClient.getFBCNetWorkApi()
//                .fbcGetBalance(ConvertToBody.ConvertToBody(req))
//                .enqueue(object : Callback<WalletBalanceModel> {
//                    override fun onResponse(call: Call<WalletBalanceModel>, response: Response<WalletBalanceModel>) {
//                        val body = response.body()
//                        val rightNum = MoneyUtils.getRightNum(body!!.balance)
//                        callBack.valueBack(rightNum)
//                    }
//
//                    override fun onFailure(call: Call<WalletBalanceModel>, t: Throwable) {
//                        callBack.valueBack("")
//                    }
//                })
    }
}