package fanrong.cwvwalled.parenter

import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.litepal.TokenInfo
import net.sourceforge.http.model.CWVCoinType

abstract class AddAssetPresenter {
    var hasCoins = mutableListOf<TokenInfo>()
    abstract fun requestAsset(inputStr: String, valueCallBack: ValueCallBack<List<TokenInfo>> ,wallet: GreWalletModel)
    abstract fun changeAssetStatus(coinBean: TokenInfo, boolean: Boolean)
}