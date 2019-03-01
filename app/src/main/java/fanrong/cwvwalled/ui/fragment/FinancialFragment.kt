package fanrong.cwvwalled.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.ui.activity.WebViewActivity
import fanrong.cwvwalled.ui.adapter.FinancialAdapter
import kotlinx.android.synthetic.main.fragment_financial.*
import java.io.Serializable
import java.util.ArrayList

class FinancialFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_financial
    }

    override fun loadData() {
    }

    override fun onClick(v: View?) {
    }


    override fun initView() {

        datas.add(FinancialItem("12%", "30天年化", "ETH锁币", "http://t.cn/Eyqsyoq"))
        datas.add(FinancialItem("6%", "30天年化", "USDT锁币", "http://t.cn/EyqstId"))
        datas.add(FinancialItem("76%", "30天年化", "Voyager ETH 对冲基金", "http://t.cn/Eyqs1Lb"))
        datas.add(FinancialItem("68%", "30天年化", "Kepler USDT 对冲基金", "http://t.cn/Eyqskx1"))

        rl_recycler.overScrollMode = View.OVER_SCROLL_NEVER
        rl_recycler.layoutManager = LinearLayoutManager(activity)

        var adapter = FinancialAdapter(R.layout.item_financial)
        rl_recycler.adapter = adapter
        adapter.newData = datas
        adapter.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val item = adapter.newData!![position]
            val bundle = Bundle()
            bundle.putString("url", item!!.financialUrl)
            startActivity(WebViewActivity::class.java, bundle)
        }
    }

//
//    @{@"interestRate":@"12%",@"cycle":@"30天年化",@"financialTitle":@"ETH锁币",@"financialUrl":@"http://t.cn/Eyqsyoq"},
//    @{@"interestRate":@"6%",@"cycle":@"30天年化",@"financialTitle":@"USDT锁币",@"financialUrl":@"http://t.cn/EyqstId"},
//    @{@"interestRate":@"76%",@"cycle":@"30天年化",@"financialTitle":@"Voyager ETH 对冲基金",@"financialUrl":@"http://t.cn/Eyqs1Lb"},
//    @{@"interestRate":@"68%",@"cycle":@"30天年化",@"financialTitle":@"Kepler USDT 对冲基金",@"financialUrl":@"http://t.cn/Eyqskx1"}


    var datas = ArrayList<FinancialItem>()

    class FinancialItem(var interestRate: String
                        , var cycle: String
                        , var financialTitle: String
                        , var financialUrl: String)
        : Serializable
}