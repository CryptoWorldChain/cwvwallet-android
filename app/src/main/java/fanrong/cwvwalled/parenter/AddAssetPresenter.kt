package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import net.sourceforge.http.model.CoinBean

abstract class AddAssetPresenter {
    var hasCoins = mutableListOf<LiteCoinBeanModel>()
    abstract fun requestAsset(inputStr: String, valueCallBack: ValueCallBack<List<CoinBean>>)
    abstract fun changeAssetStatus(coinBean: CoinBean, boolean: Boolean)
}