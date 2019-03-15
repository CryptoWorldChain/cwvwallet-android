package fanrong.cwvwalled.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import fanrong.cwvwalled.R
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.parenter.ToRMBPresenter
import fanrong.cwvwalled.parenter.WalletDetailETHPresenter
import fanrong.cwvwalled.parenter.WalletDetailFBCPresenter
import fanrong.cwvwalled.parenter.WalletDetailPresenter
import fanrong.cwvwalled.ui.adapter.EthDetailAdapter
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.MoneyUtils
import kotlinx.android.synthetic.main.activity_eth_detail.*
import net.sourceforge.http.model.spdt.TransRecordItem
import net.sourceforge.http.model.spdt.TransactionRecordResp
import xianchao.com.basiclib.extension.extStartActivity
import xianchao.com.basiclib.utils.BundleUtils
import xianchao.com.basiclib.utils.CheckedUtils

class WalletDetailActivity : BaseActivity(), View.OnClickListener {


    companion object {
        const val PARAMTER_COIN_NAME = "PARAMTER_COIN_NAME"
        fun start(context: Context, contract: String) {
            val intent = Intent(context, WalletDetailActivity::class.java)
            intent.putExtra(PARAMTER_COIN_NAME, contract)
            context.startActivity(intent)
        }
    }

    //    lateinit var balancePresenter: QueryBalancePresenter
    lateinit var coinBeanModel: LiteCoinBeanModel
    lateinit var presenter: WalletDetailPresenter

    override fun getLayoutId(): Int {
        return R.layout.activity_eth_detail
    }

    var activity: WalletDetailActivity = this

    override fun initView() {

        coinBeanModel = intent.getSerializableExtra(PageParamter.PAREMTER_LITE_COINBEAN) as LiteCoinBeanModel

        setLeftImgOnclickListener {
            finish()
        }
        setTitleText(coinBeanModel.coin_symbol!!)

        btn_transfer.setOnClickListener(this)
        btn_accept.setOnClickListener(this)

        rl_recycler.layoutManager = LinearLayoutManager(this)
        val detailAdapter = EthDetailAdapter(coinBeanModel, R.layout.item_eth_cecord)
        rl_recycler.adapter = detailAdapter
        detailAdapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val item = adapter!!.getItem(position) as TransRecordItem
                val createWith = BundleUtils.createWith(PageParamter.PAREMTER_TRANS_RECORD, item)
                createWith.putSerializable(PageParamter.PAREMTER_LITE_COINBEAN, coinBeanModel)
                extStartActivity(TransRecordActivity::class.java, createWith)
            }
        })
        tv_count_cny.text = "≈0 CNY"



        refreshLayout.isEnableRefresh = true
        refreshLayout.isEnableLoadmore = true
        refreshLayout.setEnableNestedScroll(true)
        refreshLayout.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onLoadmore(refreshlayout: RefreshLayout?) {
                presenter.pageNum++
                presenter.queryRecord(recordValueBack)
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                presenter.pageNum = 1
                refreshlayout.resetNoMoreData()
                presenter.queryRecord(recordValueBack)
            }

        })

        if ("ETH".equals(coinBeanModel.channel_name)) {
            presenter = WalletDetailETHPresenter()
        } else {
            presenter = WalletDetailFBCPresenter()
        }
        presenter.liteCoinBeanModel = coinBeanModel
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_accept -> {
                val createWith = BundleUtils.createWith(PageParamter.PAREMTER_LITE_COINBEAN, coinBeanModel)
                startActivity(ReceiptActivity::class.java, createWith)
            }
            R.id.btn_transfer -> {
                val createWith = BundleUtils.createWith(PageParamter.PAREMTER_LITE_COINBEAN, coinBeanModel)
                startActivity(TransferActivity::class.java, createWith)
            }

            else -> {
            }
        }

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }


    override fun loadData() {
        presenter.pageNum = 1
        presenter.queryRecord(recordValueBack)
        presenter.queryBalance(balanceValueCallBack)
    }

    var balanceValueCallBack = object : ValueCallBack<String?> {
        override fun valueBack(t: String?) {
            val handleDecimal = MoneyUtils.commonHandleDecimal(t)
            tv_count.text = "${handleDecimal} ${coinBeanModel.coin_symbol}"
            var coin_symbol = coinBeanModel.coin_symbol!!.replace("(c)", "")
            coin_symbol = coin_symbol.replace("(e)", "")
            ToRMBPresenter.toRMB(t!!, coin_symbol) {
                val rmbDecimal = MoneyUtils.commonRMBDecimal(it)
                tv_count_cny.text = "≈ ${rmbDecimal} CNY"
            }
        }

    }

    var recordValueBack = object : ValueCallBack<TransactionRecordResp?> {
        override fun valueBack(resp: TransactionRecordResp?) {
            if (resp == null) {
                showTopMsg("查询失败")
            }
            var tx_array = resp!!.tx_array
            val detailAdapter = rl_recycler.adapter as EthDetailAdapter
            refreshLayout.finishRefresh()
            if (CheckedUtils.isEmpty(tx_array)) {
                refreshLayout.finishLoadmore()
                return
            }
            if (presenter.pageNum == 1) {
                detailAdapter.setNewData(tx_array)
            } else {
                detailAdapter.addData(tx_array!!)
            }
            if (presenter.pageNum * 10 < resp.total_rows!!.toInt()) {
                refreshLayout.finishLoadmore(500, true, false)
            } else {
                refreshLayout.finishLoadmore(500, true, true)
            }
        }
    }

}
