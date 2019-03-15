package fanrong.cwvwalled.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import fanrong.cwvwalled.R
import fanrong.cwvwalled.http.model.Kmarket
import fanrong.cwvwalled.http.model.MarketInfoResp
import fanrong.cwvwalled.utils.MoneyUtils
import kotlinx.android.synthetic.main.item_market.view.*
import xianchao.com.basiclib.utils.CheckedUtils
import java.math.BigDecimal
import java.math.RoundingMode

class MarketAdapter(var layoutId: Int) : BaseQuickAdapter<Kmarket, BaseViewHolder>(layoutId) {

    override fun convert(helper: BaseViewHolder, item: Kmarket) {
        helper.setText(R.id.tv_coin_name, item.coin)
        var total = ""
        if (CheckedUtils.nonEmpty(item.rmbcurrencyvalue)) {
            total = BigDecimal(item.rmbcurrencyvalue).divide(BigDecimal(Math.pow(10.0, 7.0))).setScale(2, RoundingMode.DOWN).toString()
        }
        helper.setText(R.id.tv_total, "约${total}千万")
        helper.setText(R.id.tv_price_rmb, "￥ " + MoneyUtils.commonRMBDecimal(item.rmbclose))
        var decline = BigDecimal.ZERO
        if (CheckedUtils.nonEmpty(item.updown)) {
            decline = BigDecimal(item.updown).multiply(BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)
        }
        if (decline.compareTo(BigDecimal.ZERO) == 1) {
            helper.setBackgroundRes(R.id.tv_advance_decline, R.drawable.market_add_bg)
        } else {
            helper.setBackgroundRes(R.id.tv_advance_decline, R.drawable.market_subtract_bg)
        }
        helper.setText(R.id.tv_advance_decline, decline.toString() + "%")


    }

}