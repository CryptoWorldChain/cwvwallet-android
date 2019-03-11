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

        val before = (System.currentTimeMillis() - item.created_time!!.toLong()) / 1000
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

        if (coinBeanModel.sourceAddr.equals(item.from_addr, true)) {
            helper.setText(R.id.tv_address, item.to_addr)
            helper.setText(R.id.tv_count, "-" + MoneyUtils.commonHandleDecimal(MoneyUtils.getRightNum(item.value)))
            helper.setTextColor(R.id.tv_count, Color.parseColor("#eca703"))
        } else {
            helper.setText(R.id.tv_address, item.from_addr)
            helper.setText(R.id.tv_count, "+" + MoneyUtils.commonHandleDecimal(MoneyUtils.getRightNum(item.value)))
            helper.setTextColor(R.id.tv_count, Color.parseColor("#00b858"))
        }



        if ("成功".equals(item.des)) {
            // 成功
            // 初始地址 和自己钱包地址一样 是转出
            if (coinBeanModel.sourceAddr == item.from_addr) {
//                helper.setImageResource(R.id.iv_image, R.drawable.eth_item_status_out)
                helper.getView<TextView>(R.id.tv_status).visibility = View.INVISIBLE
            } else {
//                helper.setImageResource(R.id.iv_image, R.drawable.eth_item_status_in)
                helper.getView<TextView>(R.id.tv_status).visibility = View.INVISIBLE
            }
        } else if ("失败".equals(item.des)) {
            // 失败
//            helper.setImageResource(R.id.iv_image, R.drawable.eth_item_status_error)
            helper.setText(R.id.tv_count, "交易失败")
            helper.setTextColor(R.id.tv_count, Color.parseColor("#fd674b"))
            helper.setText(R.id.tv_status, item.des)
            helper.getView<TextView>(R.id.tv_status).visibility = View.VISIBLE
        } else {
            // icon 等待
//            helper.setImageResource(R.id.iv_image, R.drawable.eth_item_status_wait)
            helper.setText(R.id.tv_status, item.des)
            helper.setTextColor(R.id.tv_count, Color.parseColor("#4a90e2"))
            helper.getView<TextView>(R.id.tv_status).visibility = View.VISIBLE
        }
    }
}