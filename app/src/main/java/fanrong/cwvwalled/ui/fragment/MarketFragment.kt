package fanrong.cwvwalled.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.Kmarket
import fanrong.cwvwalled.http.model.MarketInfoResp
import fanrong.cwvwalled.http.model.ToRMBReq
import fanrong.cwvwalled.ui.activity.CoinMarketActivity
import fanrong.cwvwalled.ui.adapter.MarketAdapter
import kotlinx.android.synthetic.main.fragment_market.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_market
    }


    lateinit var adapter: MarketAdapter

    override fun initView() {
        refreshlayout.setOnRefreshListener {
            loadData()
        }
        rl_recycler.layoutManager = LinearLayoutManager(activity)
        adapter = MarketAdapter(R.layout.item_market)
        adapter.onItemClickListener = object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
//                Kmarket
                val bundle = Bundle()
                bundle.putSerializable(PageParamter.PAREMTER_KMARKET, adapter!!.data[position] as Kmarket)
                startActivity(CoinMarketActivity::class.java, bundle)
            }
        }
        rl_recycler.adapter = adapter
        loadData()
    }

    override fun loadData() {
        querydata()
    }

    private fun querydata() {
        val toRMBReq = ToRMBReq()
        toRMBReq.period = "60min"
        RetrofitClient.getNetWorkApi()
                .marketInfo(ConvertToBody.ConvertToBody(toRMBReq))
                .enqueue(object : Callback<MarketInfoResp> {
                    override fun onFailure(call: Call<MarketInfoResp>, t: Throwable) {
                        refreshlayout.finishRefresh()
                    }

                    override fun onResponse(call: Call<MarketInfoResp>, response: Response<MarketInfoResp>) {
                        refreshlayout.finishRefresh()
                        adapter.setNewData(response.body()!!.kmarket)
                    }

                })

    }

    override fun onClick(v: View?) {
    }

}