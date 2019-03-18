package fanrong.cwvwalled.ui.activity

import android.view.View
import android.widget.RadioGroup
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.Kmarket
import fanrong.cwvwalled.http.model.ToRMBReq
import fanrong.cwvwalled.http.model.response.ToRMBResp
import fanrong.cwvwalled.ui.view.LineChartView
import fanrong.cwvwalled.utils.DecimalUtils
import fanrong.cwvwalled.utils.MoneyUtils
import kotlinx.android.synthetic.main.activity_coin_market.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class CoinMarketActivity : BaseActivity() {


    lateinit var kmarket: Kmarket
    override fun initView() {
        kmarket = intent.getSerializableExtra(PageParamter.PAREMTER_KMARKET) as Kmarket
        var decline = BigDecimal.ZERO
        if (CheckedUtils.nonEmpty(kmarket.updown)) {
            decline = BigDecimal(kmarket.updown).multiply(BigDecimal(100)).setScale(2)
        }
        if (decline.compareTo(BigDecimal.ZERO) == 1) {
            tv_updown.setBackgroundResource(R.drawable.market_add_bg)
        } else {
            tv_updown.setBackgroundResource(R.drawable.market_subtract_bg)
        }
        tv_updown.text = decline.toString() + "%"

        tv_top_close.text = "$" + DecimalUtils.scale2Down(kmarket.close!!)
        tv_top_rmb_close.text = "￥" + DecimalUtils.scale2Down(kmarket.rmbclose!!)
        setLeftImgOnclickListener {
            finish()
        }
        setTitleText("行情详情")

        lcv_chartview.setShowTable(true)
        lcv_chartview.setStepSpace(60)
        tv_coin_name.text = kmarket.coin
        rg_market_tab.check(R.id.rb_left)
        rg_market_tab.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {

            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                when (checkedId) {
                    R.id.rb_left -> {
                        loadKline("60min")
                    }
                    R.id.rb_middle -> {
                        loadKline("1day")
                    }
                    R.id.rb_right -> {
                        loadKline("1week")
                    }
                }
            }

        })
        loadKline("60min")

    }

    override fun onClick(v: View) {
    }

    override fun loadData() {
    }

    private fun loadKline(period: String) {
        val rmbReq = ToRMBReq()
        rmbReq.period = period
        rmbReq.coin = kmarket.coin
        rmbReq.size = "6"
        RetrofitClient.getNetWorkApi()
                .toRMB(ConvertToBody.ConvertToBody(rmbReq))
                .enqueue(object : Callback<ToRMBResp> {

                    override fun onFailure(call: Call<ToRMBResp>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<ToRMBResp>, response: Response<ToRMBResp>) {
                        if (CheckedUtils.nonEmpty(response.body()!!.kline)) {
                            val first = response.body()!!.kline[0]
                            tv_open.text = "$" + DecimalUtils.scale2Down(first.open)
                            tv_high.text = "$" + DecimalUtils.scale2Down(first.high)
                            tv_low.text = "$" + DecimalUtils.scale2Down(first.low)
                            val toString = "$" + BigDecimal(first.vol).divide(BigDecimal(Math.pow(10.0, 7.0))).setScale(2, RoundingMode.DOWN).toString()
                            tv_trans.text = "约${toString}千万"
                        }
                        refreshChart(response.body()!!.kline, period)
                    }

                })
    }

    var chartXText = arrayOfNulls<String>(6)

    fun refreshChart(kline: MutableList<ToRMBResp.FloatNote>, period: String) {
        if (CheckedUtils.isEmpty(kline)) {
            return
        }

        when (period) {
            "60min" -> {
                chartXText.forEachIndexed { index, s ->
                    val instance = Calendar.getInstance()
                    instance.set(Calendar.HOUR_OF_DAY, instance.get(Calendar.HOUR_OF_DAY) - index)
                    chartXText[chartXText.size - index - 1] = "${instance.get(Calendar.HOUR_OF_DAY)}:00"
                }
            }
            "1day" -> {
                chartXText.forEachIndexed { index, s ->
                    val instance = Calendar.getInstance()
                    instance.set(Calendar.DAY_OF_YEAR, instance.get(Calendar.DAY_OF_YEAR) - index)
                    chartXText[chartXText.size - index - 1] = "${instance.get(Calendar.MONTH) + 1}/${instance.get(Calendar.DAY_OF_MONTH)}"
                }
            }
            "1week" -> {
                chartXText.forEachIndexed { index, s ->
                    val instance = Calendar.getInstance()
                    instance.set(Calendar.DAY_OF_YEAR, instance.get(Calendar.DAY_OF_YEAR) - (index * 7))
                    chartXText[chartXText.size - index - 1] = "${instance.get(Calendar.MONTH) + 1}/${instance.get(Calendar.DAY_OF_MONTH)}"
                }
            }
        }

        val datas = ArrayList<LineChartView.Data>()
        kline.forEachIndexed { index, floatNote ->
            val data = LineChartView.Data(BigDecimal(kline[index].close).setScale(3, RoundingMode.DOWN).toFloat())
            data.xText = chartXText[index]
            datas.add(data)
        }
        lcv_chartview.setData(datas)


    }

    private fun initXtextWithHour() {

        chartXText.forEachIndexed { index, s ->
            val instance = Calendar.getInstance()
            instance.set(Calendar.HOUR_OF_DAY, instance.get(Calendar.HOUR_OF_DAY) - index)
            chartXText[chartXText.size - index - 1] = "${instance.get(Calendar.HOUR_OF_DAY)}:00"
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_coin_market
    }
}
