package fanrong.cwvwalled.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.parenter.*
import fanrong.cwvwalled.ui.adapter.EthDetailAdapter
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.MoneyUtils
import kotlinx.android.synthetic.main.activity_eth_detail.*
import net.sourceforge.http.model.spdt.TransRecordItem
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
    lateinit var balancePresenter: BalancePresenter

    override fun getLayoutId(): Int {
        return R.layout.activity_eth_detail
    }

    var activity: WalletDetailActivity = this

    lateinit var tv_count: TextView
    lateinit var tv_count_cny: TextView

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

        val view = layoutInflater.inflate(R.layout.activity_eth_detail_header, null)
        tv_count = view.findViewById(R.id.tv_count)
        tv_count_cny = view.findViewById(R.id.tv_count_cny)
        tv_count_cny.text = "≈ ￥ 0"

        detailAdapter.addHeaderView(view)
        refreshLayout.isEnableRefresh = true
        refreshLayout.isEnableLoadmore = true
        refreshLayout.setEnableNestedScroll(true)
        refreshLayout.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onLoadmore(refreshlayout: RefreshLayout?) {
                presenter.pageNum++
                presenter.queryRecord(this@WalletDetailActivity::getRecord)
            }

            override fun onRefresh(refreshlayout: RefreshLayout) {
                refreshlayout.resetNoMoreData()
                loadData()
            }

        })

        if ("ETH".equals(coinBeanModel.channel_name)) {
            presenter = WalletDetailETHPresenter()
        } else {
            presenter = WalletDetailFBCPresenter()
        }
        presenter.bindLifecycleOwner(this)
        presenter.liteCoinBeanModel = coinBeanModel
        balancePresenter = BalancePresenter()
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
        getBalance()
        getTransRecord()
    }

    private fun getTransRecord() {
        presenter.pageNum = 1
        presenter.queryRecord(this::getRecord)
    }


    fun getRecord(errorCode: String, walletTrans: MutableList<TransRecordItem>?) {


        if (!"1".equals(errorCode)) {
            showTopMsg("查询失败")
            return
        }
        var transItems = walletTrans?: mutableListOf()
        val detailAdapter = rl_recycler.adapter as EthDetailAdapter
        refreshLayout.finishRefresh()
        if (CheckedUtils.isEmpty(transItems)) {
            refreshLayout.finishLoadmore()
            return
        }
        if (presenter.pageNum == 1) {
            detailAdapter.setNewData(transItems)
        } else {
            detailAdapter.addData(transItems!!)
        }

        if (transItems.isNotEmpty()) {
            refreshLayout.finishLoadmore(500, true, false)
        } else {
            refreshLayout.finishLoadmore(500, true, true)
        }
    }

    private fun getBalance() {
        balancePresenter.getAddressBalance(coinBeanModel.sourceAddr
                ?: "") { resCode, balanceValue ->

            if ("1".equals(resCode)) {
                val symbol = AppUtils.getRealSymbol(coinBeanModel.coin_symbol)
                val balanceToken = balanceValue?.tokensMap?.get(symbol)

                val handleDecimal = MoneyUtils.commonHandleDecimal(balanceToken?.balance)
                tv_count.text = "${handleDecimal} ${coinBeanModel.coin_symbol}"
                tv_count_cny.text = "≈ ￥ ${handleDecimal}"
            }
        }
    }

}
