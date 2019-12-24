package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.common.ThreadExecutor
import fanrong.cwvwalled.http.engine.NetTools
import fanrong.cwvwalled.http.model.BalanceAccount
import fanrong.cwvwalled.http.model.BalanceResp
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.MoneyUtils
import fanrong.cwvwalled.utils.SWLog
import org.cwv.client.sdk.HiChain
import xianchao.com.basiclib.BasicLibComponant
import xianchao.com.basiclib.utils.checkNotEmpty

class BalancePresenter {


    fun getAddressCoinBalance(address: String, coin: String,
                              callBack: (balance: String?) -> Unit) {

        ThreadExecutor.execute(object : Runnable {
            override fun run() {

                var result = HiChain.getUserAccountInfo(address) ?: ""
                SWLog.e("BalancePresenter-getAddressCoinBalance::" + result)
                var resultObj = NetTools.formatJson(result, BalanceResp::class.java)

                if (resultObj == null) {
                    resultObj = BalanceResp("-1")
                }

                if ("1".equals(resultObj.retCode)) {

                    if ("CWV".equals(coin)) {

                        BasicLibComponant.postMainThread {
                            callBack(MoneyUtils.getRightNum(resultObj?.account?.value?.balance))
                        }
                        return@run
                    } else {
                        resultObj?.account?.value?.tokens?.forEach {
                            if (coin.equals(it.token)) {
                                BasicLibComponant.postMainThread {
                                    callBack(MoneyUtils.getRightNum(it.balance))
                                }
                                return@run
                            }
                        }
                    }
                }


                SWLog.e(result)
                BasicLibComponant.postMainThread {
                    callBack("0.00")
                }
            }
        })
    }


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

                if ("1".equals(resultObj.retCode)) {
                    var tokensMap = hashMapOf<String, BalanceAccount.BalanceToken>()
                    resultObj?.account?.value?.tokensMap = tokensMap

                    val balanceToken = BalanceAccount.BalanceToken()
                    balanceToken.token = "CWV"
                    balanceToken.balance = MoneyUtils.getRightNum(resultObj?.account?.value?.balance)
                    resultObj?.account?.value?.tokensMap?.put("CWV", balanceToken)

                    if (resultObj.account != null
                            && resultObj.account?.value != null
                            && resultObj?.account?.value?.tokens.checkNotEmpty()) {

                        resultObj?.account?.value?.tokens?.forEach {
                            it.balance = MoneyUtils.getRightNum(it.balance)
                            tokensMap.put(it.token ?: "", it)
                        }
                    }

                }

                SWLog.e(result)
                BasicLibComponant.postMainThread {
                    callBack(resultObj.retCode!!, resultObj?.account?.value)
                }
            }
        })
    }
}