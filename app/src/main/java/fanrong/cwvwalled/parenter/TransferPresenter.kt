package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.http.model.TransferReq
import fanrong.cwvwalled.litepal.LiteCoinBeanModel

abstract class TransferPresenter {


    var coinBeanModel: LiteCoinBeanModel

    constructor(coinBeanModel: LiteCoinBeanModel) {
        this.coinBeanModel = coinBeanModel
    }

    abstract fun transferMoney(req: TransferReq, callBack: TransferResultCallBack)
    abstract fun getNonce(callBack: ValueCallBack<String?>)
    abstract fun getBalance(callBack: ValueCallBack<String?>)
    abstract fun getGasPrice(callBack: ValueCallBack<String?>)

    interface TransferResultCallBack {
        fun success()

        fun failed(msg: String)
    }
}