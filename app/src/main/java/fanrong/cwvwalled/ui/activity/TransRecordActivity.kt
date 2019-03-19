package fanrong.cwvwalled.ui.activity

import android.view.View
import com.facebook.common.util.Hex
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.MoneyUtils
import kotlinx.android.synthetic.main.activity_trans_record.*
import net.sourceforge.http.model.spdt.TransRecordItem
import org.json.JSONObject
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.XcJsonUtils
import xianchao.com.basiclib.utils.checkIsEmpty
import java.text.SimpleDateFormat
import kotlin.Exception

class TransRecordActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_trans_record
    }

    lateinit var transRecord: TransRecordItem
    lateinit var coinBeanModel: LiteCoinBeanModel

    override fun initView() {

        transRecord = intent.getSerializableExtra(PageParamter.PAREMTER_TRANS_RECORD) as TransRecordItem
        coinBeanModel = intent.getSerializableExtra(PageParamter.PAREMTER_LITE_COINBEAN) as LiteCoinBeanModel
        setLeftImgOnclickListener { finish() }
        setTitleText("交易记录详情")

        tv_trans_id.setOnClickListener(this)

        var fuhao = if (getSelfAddr().equals(transRecord.from_addr)) "-" else "+"
        tv_count.text = fuhao + MoneyUtils.commonRMBDecimal(MoneyUtils.getRightNum(transRecord.value)) + " ${coinBeanModel.coin_symbol}"
        tv_to_address.text = transRecord.to_addr
        tv_from_address.text = transRecord.from_addr
        tv_tip.text = MoneyUtils.getDownTip(transRecord.gas_used) + " ether"

        if (CheckedUtils.nonEmpty(transRecord.ex_data)) {

            var ex_data = ""
            try {
                ex_data = String(Hex.decodeHex(transRecord.ex_data))
            } catch (e: Exception) {
                ex_data = transRecord.ex_data!!
            }
            if ("CWV".equals(coinBeanModel.channel_name)) {
                tv_tip.text = "无"
            }

            if (CheckedUtils.isJson(ex_data)) {
                tv_remark.text = XcJsonUtils.getString(JSONObject(ex_data), "note")
            } else {
                tv_remark.text = ex_data
            }
        }

        tv_trans_id.text = transRecord.tx_id
        tv_block_id.text = transRecord.block_number
        tv_trans_time.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transRecord.created_time)



        if ("成功".equals(transRecord.des)) {
            // 成功
            if (getSelfAddr().equals(transRecord.from_addr, true)) {
                iv_trans_status.setImageResource(R.drawable.detail_item_out)
            } else {
                iv_trans_status.setImageResource(R.drawable.detail_item_in)
            }
        } else if ("失败".equals(transRecord.des)) {
            // 失败
            iv_trans_status.setImageResource(R.drawable.detail_item_error)

        } else {
            // icon 等待
            iv_trans_status.setImageResource(R.drawable.detail_item_wait)
        }


        loadData()
    }

    fun getSelfAddr(): String {
        return coinBeanModel.sourceAddr!!
    }

    override fun onClick(v: View) {
        AppUtils.clipboardString(this, tv_trans_id.text.toString())
        showTopMsg("已复制")
    }

    override fun loadData() {
    }
}
