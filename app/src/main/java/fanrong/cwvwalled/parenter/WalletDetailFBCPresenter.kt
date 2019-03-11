package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.R.id.*
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.GetBalanceReq
import fanrong.cwvwalled.http.model.WalletBalanceModel
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.utils.MoneyUtils
import net.sourceforge.http.model.spdt.TransactionRecordReq
import net.sourceforge.http.model.spdt.TransactionRecordResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils
import java.math.BigDecimal

class WalletDetailFBCPresenter() : WalletDetailPresenter() {

    override fun queryRecord(valueCallBack: ValueCallBack<TransactionRecordResp?>) {

        val recordReq = TransactionRecordReq(Constants.DAPP_ID)
        recordReq.account_addr = liteCoinBeanModel.sourceAddr
        recordReq.node_url = GreNodeOperator.queryCWVnode().node_url
        recordReq.limit = "" + pageSize
        recordReq.page_num = "" + pageNum
        liteCoinBeanModel.coin_symbol = liteCoinBeanModel.coin_symbol!!.replace("(e)", "")
        if ("ETH".equals(liteCoinBeanModel.coin_symbol)) {
            recordReq.contract_addr = "null"
        } else {
            recordReq.contract_addr = liteCoinBeanModel.contract_addr
        }

        RetrofitClient.getFBCNetWorkApi()
                .queryTransactionRecord(ConvertToBody.ConvertToBody(recordReq))
                .enqueue(object : Callback<TransactionRecordResp> {
                    override fun onFailure(call: Call<TransactionRecordResp>, t: Throwable) {
                        valueCallBack.valueBack(null)
                    }

                    override fun onResponse(call: Call<TransactionRecordResp>, response: Response<TransactionRecordResp>) {
                        valueCallBack.valueBack(response.body()!!)
                    }

                })
    }

    override fun queryBalance(valueCallBack: ValueCallBack<String?>) {

        val gnodeModel = GreNodeOperator.queryETHnode()

        var constractAddr = liteCoinBeanModel.contract_addr
        if (CheckedUtils.isEmpty(constractAddr)) {
            constractAddr = ""
        }

        val req = GetBalanceReq()
        req.dapp_id = Constants.DAPP_ID
        req.node_url = gnodeModel.node_url
        req.address = liteCoinBeanModel.sourceAddr
        req.contract_addr = constractAddr

        RetrofitClient.getFBCNetWorkApi()
                .fbcGetBalance(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<WalletBalanceModel> {
                    override fun onResponse(call: Call<WalletBalanceModel>, response: Response<WalletBalanceModel>) {
                        val body = response.body()
                        val rightNum = MoneyUtils.getRightNum(body!!.balance)
                        valueCallBack.valueBack(rightNum)
                    }

                    override fun onFailure(call: Call<WalletBalanceModel>, t: Throwable) {
                        valueCallBack.valueBack("")
                    }
                })
    }
}