package fanrong.cwvwalled.litepal

import android.content.ContentValues
import net.sourceforge.http.model.CoinBean
import org.litepal.LitePal

object LiteCoinBeanOperator {

    fun copyCoinBean(coinBean: CoinBean): LiteCoinBeanModel {
        val beanModel = LiteCoinBeanModel(coinBean.coin_name)
        beanModel.channel_name = coinBean.channel_name
        beanModel.coin_decimals = coinBean.coin_decimals
        beanModel.coin_icon = coinBean.coin_icon
        beanModel.coin_symbol = coinBean.coin_symbol
        beanModel.coin_total_supply = coinBean.coin_total_supply
        beanModel.contract_addr = coinBean.contract_addr
        beanModel.sourceAddr = coinBean.sourceAddr
        beanModel.walletName = coinBean.walletName
        return beanModel
    }

    fun findAllETHs(): MutableList<LiteCoinBeanModel> {
        return LitePal.where("channel_name like ?", "ETH")
                .find(LiteCoinBeanModel::class.java)
    }

    fun findAllCWVs(): MutableList<LiteCoinBeanModel> {
        return LitePal.where("channel_name like ?", "CWV")
                .find(LiteCoinBeanModel::class.java)
    }


    fun findAllFromParent(addr: String): MutableList<LiteCoinBeanModel> {
        return LitePal.where("sourceAddr like ?", addr).find(LiteCoinBeanModel::class.java)
    }

    fun updateAllWalletName(walletModel: GreWalletModel) {
        val contentValues = ContentValues()
        contentValues.put("walletName", walletModel.walletName)
        LitePal.updateAll(LiteCoinBeanModel::class.java, contentValues, "sourceAddr like ?", walletModel.address)

    }


}