package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.common.ThreadExecutor
import fanrong.cwvwalled.http.engine.NetTools
import fanrong.cwvwalled.http.model.BalanceAccount
import fanrong.cwvwalled.http.model.BalanceResp
import fanrong.cwvwalled.utils.SWLog
import org.cwv.client.sdk.HiChain
import xianchao.com.basiclib.BasicLibComponant
import xianchao.com.basiclib.utils.checkNotEmpty

class BalancePresenter {

    fun getAddressBalance(address: String,
                          callBack: (resCode: String, balanceValue: BalanceAccount.BalanceValue?) -> Unit) {

        ThreadExecutor.execute(object : Runnable {
            override fun run() {

                var result = HiChain.getUserAccountInfo(address) ?: ""
                SWLog.e("BalancePresenter-getAddressBalance::" + result)
                var resultObj = NetTools.formatJson(result, BalanceResp::class.java)

                if (resultObj == null) {
                    resultObj = BalanceResp("-1")
                }

                if ("1".equals(resultObj.retCode)
                        && resultObj.account != null
                        && resultObj.account?.value != null
                        && resultObj?.account?.value?.tokens.checkNotEmpty()) {

                    var tokensMap = hashMapOf<String, BalanceAccount.BalanceToken>()
                    resultObj?.account?.value?.tokens?.forEach {
                        tokensMap.put(it.token ?: "", it)
                    }
                    resultObj?.account?.value?.tokensMap = tokensMap
                }


                SWLog.e(result)
                BasicLibComponant.postMainThread {
                    callBack(resultObj.retCode!!, resultObj?.account?.value)
                }
            }
        })
    }
}