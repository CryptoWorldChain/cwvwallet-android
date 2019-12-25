package fanrong.cwvwalled.ui.adapter


import android.text.method.HideReturnsTransformationMethod
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import fanrong.cwvwalled.R
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.utils.MoneyUtils
import fanrong.cwvwalled.utils.WordReplacement
import xianchao.com.basiclib.utils.CheckedUtils

import java.math.BigDecimal

/**
 * Created by terry.c on 12/03/2018.
 */

class HomeAssertsAdapter(layoutResId: Int) : BaseQuickAdapter<LiteCoinBeanModel, BaseViewHolder>(layoutResId) {

    var greWalletModel: GreWalletModel? = null


    override fun convert(helper: BaseViewHolder, item: LiteCoinBeanModel) {
        val isHiddenAmount = greWalletModel?.isShowRmb ?: false
        if ("ETH".equals(item.coin_name)) {
            helper.setImageResource(R.id.iv_image, R.drawable.common_eth_icon)
        } else {
            helper.setImageResource(R.id.iv_image, R.drawable.common_cwv_icon)
        }

        var tvAcount = helper.getView<TextView>(R.id.tv_count)
        tvAcount.transformationMethod = null
        helper.setText(R.id.tv_count, MoneyUtils.commonHandleDecimal(item.count))
        if (isHiddenAmount) {
            tvAcount.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
        } else {
            tvAcount.setTransformationMethod(WordReplacement.getInstance())
        }
        helper.setText(R.id.tv_name, item.coin_symbol)
        val handleDecimal = (item.countCNY)

        if (CheckedUtils.isEmpty(handleDecimal) || BigDecimal.ZERO.compareTo(BigDecimal(handleDecimal)) == 0) {
            helper.setText(R.id.tv_countCNY, "--")
        } else {
            helper.setText(R.id.tv_countCNY, "¥ " + MoneyUtils.commonRMBDecimal(handleDecimal))
        }
    }


}
