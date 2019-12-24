package fanrong.cwvwalled.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import fanrong.cwvwalled.R
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.utils.MoneyUtils
import net.sourceforge.http.model.spdt.TransRecordItem

class EthDetailAdapter(var coinBeanModel: LiteCoinBeanModel, layoutId: Int) : BaseQuickAdapter<TransRecordItem, BaseViewHolder>(layoutId) {
    override fun convert(helper: BaseViewHolder, item: TransRecordItem) {

        val before = (System.currentTimeMillis() - (item.transTime?.toLong() ?: 0)) / 1000
        if (before <= 0) {
            helper.setText(R.id.tv_time, "刚刚")
        } else if (before < 60) {
            helper.setText(R.id.tv_time, "$before 秒前")
        } else if (before < 60 * 60) {
            helper.setText(R.id.tv_time, "${before / 60} 分钟前")
        } else if (before < 60 * 60 * 24) {
            helper.setText(R.id.tv_time, "${before / (60 * 60)} 小时前")
        } else if (before < 60 * 60 * 24 * 30) {
            helper.setText(R.id.tv_time, "${before / (60 * 60 * 24)} 天前")
        } else {
            helper.setText(R.id.tv_time, "${before / (60 * 60 * 24 * 30)} 月前")

        }

        if (item.isTransOut(coinBeanModel.sourceAddr)) {
            helper.setImageResource(R.id.iv_image, R.drawable.detail_item_out)
            helper.setText(R.id.tv_address, item.getTransInAddr())
            helper.setText(R.id.tv_count, "-" + MoneyUtils.commonHandleDecimal(MoneyUtils.getRightNum(item.amount)))
            helper.setTextColor(R.id.tv_count, Color.parseColor("#b077fa"))
        } else {
            helper.setImageResource(R.id.iv_image, R.drawable.detail_item_in)
            helper.setText(R.id.tv_address, item.getTransInAddr())
            helper.setText(R.id.tv_count, "+" + MoneyUtils.commonHandleDecimal(MoneyUtils.getRightNum(item.amount)))
            helper.setTextColor(R.id.tv_count, Color.parseColor("#7cb1f9"))
        }



        if ("D".equals(item.status)) {
            // 成功
            // 初始地址 和自己钱包地址一样 是转出
            helper.getView<TextView>(R.id.tv_status).visibility = View.INVISIBLE
        } else if ("E".equals(item.status)) {
            // 失败
            helper.setImageResource(R.id.iv_image, R.drawable.detail_item_error)
            helper.setText(R.id.tv_count, "交易失败")
            helper.setTextColor(R.id.tv_count, Color.parseColor("#fd674b"))
            helper.setText(R.id.tv_status, item.des)
            helper.getView<TextView>(R.id.tv_status).visibility = View.VISIBLE
        } else {
            // icon 等待
            helper.setImageResource(R.id.iv_image, R.drawable.detail_item_wait)
            helper.setText(R.id.tv_status, item.des)
            helper.setTextColor(R.id.tv_count, Color.parseColor("#3090ff"))
            helper.getView<TextView>(R.id.tv_status).visibility = View.VISIBLE
        }
    }
}