package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.BasePresenter
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import net.sourceforge.http.model.spdt.TransRecordItem
import net.sourceforge.http.model.spdt.TransactionRecordResp

abstract class WalletDetailPresenter : BasePresenter() {
    lateinit var liteCoinBeanModel: LiteCoinBeanModel
    var pageNum: Int = 0
    var pageSize: Int = 10
    abstract fun queryRecord(valueCallBack: ValueCallBack<TransactionRecordResp?>)
    abstract fun queryBalance(valueCallBack: ValueCallBack<String?>)

    open fun queryRecord(callBack: (errorCode: String, walletTrans: MutableList<TransRecordItem>?, noMoreData: Boolean) -> Unit) {

    }

}