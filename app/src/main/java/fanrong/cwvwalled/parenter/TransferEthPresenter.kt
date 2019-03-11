package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.http.model.TransferReq
import fanrong.cwvwalled.litepal.LiteCoinBeanModel

class TransferEthPresenter : TransferPresenter {
    constructor(coinBeanModel: LiteCoinBeanModel) : super(coinBeanModel)

    override fun transferMoney(req: TransferReq, callBack: TransferResultCallBack) {
    }

    override fun getNonce(callBack: ValueCallBack<String?>) {
    }

    override fun getBalance(callBack: ValueCallBack<String?>) {
    }
}