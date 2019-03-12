package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.ToRMBReq
import fanrong.cwvwalled.http.model.response.ToRMBResp
import fanrong.cwvwalled.utils.MoneyUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ToRMBPresenter {
    fun toRMB(money: String, coin: String, callBcak: (rmb: String) -> Unit) {
        val toRMBReq = ToRMBReq()
        toRMBReq.dapp_id = Constants.DAPP_ID
        toRMBReq.coin = coin
        toRMBReq.period = "60min"
        toRMBReq.size = "1"
        RetrofitClient.getNetWorkApi()
                .toRMB(ConvertToBody.ConvertToBody(toRMBReq))
                .enqueue(object : Callback<ToRMBResp> {
                    override fun onResponse(call: Call<ToRMBResp>, response: Response<ToRMBResp>) {
                        val body = response.body()
                        if (body!!.kline != null && !body!!.kline.isEmpty()) {
                            val floatNote = body!!.kline.get(0)
                            val multiplyValue = MoneyUtils.getMultiplyValue(money, floatNote.rmbclose)
                            callBcak(multiplyValue)
                        }
                    }

                    override fun onFailure(call: Call<ToRMBResp>, t: Throwable) {

                    }
                })
    }
}