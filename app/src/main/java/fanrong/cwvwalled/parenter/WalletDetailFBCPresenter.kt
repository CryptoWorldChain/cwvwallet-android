package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.extension.bindLifecycleOwner
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.utils.AppUtils
import net.sourceforge.http.model.spdt.TransRecordItem
import net.sourceforge.http.model.spdt.TransactionRecordReq
import net.sourceforge.http.model.spdt.TransactionRecordResp
import org.cwv.client.sdk.HiChain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletDetailFBCPresenter() : WalletDetailPresenter() {

    override fun queryRecord(callBack: (errorCode: String,
                                        walletTrans: MutableList<TransRecordItem>?, noMoreData: Boolean) -> Unit) {
        val recordReq = TransactionRecordReq(Constants.DAPP_ID)

        recordReq.account_addr = liteCoinBeanModel.sourceAddr
        var coin_symbol = AppUtils.getRealSymbol(liteCoinBeanModel.coin_symbol)
        if ("CWV".equals(coin_symbol)) {
            recordReq.coinType = "1"
        } else {
            recordReq.coinType = coin_symbol
        }
        recordReq.limit = "" + pageSize
        recordReq.skip = "" + (pageSize * (pageNum - 1))
        recordReq.address = liteCoinBeanModel.sourceAddr

        RetrofitClient.getFBCNetWorkApi()
                .queryTransactionRecord(ConvertToBody.ConvertToBody(recordReq))
                .bindLifecycleOwner(lifecycleOwner)
                .enqueue(object : Callback<TransactionRecordResp> {
                    override fun onFailure(call: Call<TransactionRecordResp>, t: Throwable) {
                        callBack("-1", null, false)
                    }

                    override fun onResponse(call: Call<TransactionRecordResp>, response: Response<TransactionRecordResp>) {
                        if ("1".equals(response?.body()?.err_code)) {
                            var noMoreData = (response?.body()?.total_rows?.toInt()?: 0) <= (pageSize * pageNum)
                            callBack("1", response?.body()?.walletTrans, noMoreData)
                        } else {
                            callBack("-1", null, false)
                        }
                    }

                })

    }

    override fun queryRecord(valueCallBack: ValueCallBack<TransactionRecordResp?>) {
    }

    override fun queryBalance(valueCallBack: ValueCallBack<String?>) {
    }
}