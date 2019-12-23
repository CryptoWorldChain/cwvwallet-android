package fanrong.cwvwalled.ui.adapter


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import fanrong.cwvwalled.R
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.litepal.TokenInfo
import fanrong.cwvwalled.utils.MoneyUtils
import net.sourceforge.http.model.CWVCoinType
import xianchao.com.basiclib.utils.CheckedUtils

import java.math.BigDecimal

/**
 * Created by terry.c on 12/03/2018.
 */

class HomeAssertsAdapter(layoutResId: Int) : BaseQuickAdapter<TokenInfo, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: TokenInfo) {

//        if ("ETH".equals(item.channel_name)) {
//            helper.setImageResource(R.id.iv_image, R.drawable.common_eth_icon)
//        } else {
            helper.setImageResource(R.id.iv_image, R.drawable.common_cwv_icon)
 //       }
        helper.setText(R.id.tv_name, item.tokenName)
 //       helper.setText(R.id.tv_count, MoneyUtils.commonHandleDecimal(item.count))
//        val handleDecimal = (item.countCNY)
//
//        if (CheckedUtils.isEmpty(handleDecimal) || BigDecimal.ZERO.compareTo(BigDecimal(handleDecimal)) == 0) {
//            helper.setText(R.id.tv_countCNY, "--")
//        } else {
//            helper.setText(R.id.tv_countCNY, "¥ " + MoneyUtils.commonRMBDecimal(handleDecimal))
//        }
    }


}
