package fanrong.cwvwalled.ui.fragment

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import fanrong.cwvwalled.R
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.eventbus.*
import fanrong.cwvwalled.litepal.*
import fanrong.cwvwalled.parenter.BalancePresenter
import fanrong.cwvwalled.parenter.NodePresenter
import fanrong.cwvwalled.parenter.TransferFbcPresenter
import fanrong.cwvwalled.ui.activity.AddAssetActivity
import fanrong.cwvwalled.ui.activity.AllWalletActivity
import fanrong.cwvwalled.ui.activity.ImportWalletTypeActivity
import fanrong.cwvwalled.ui.activity.WalletDetailActivity
import fanrong.cwvwalled.ui.adapter.HomeAssertsAdapter
import fanrong.cwvwalled.ui.adapter.HomeCardAdatper
import fanrong.cwvwalled.ui.view.MyViewPager
import fanrong.cwvwalled.ui.view.ZoomOutPageTransformer
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.MoneyUtils
import fanrong.cwvwalled.utils.SWLog
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.rl_recycler
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xianchao.com.basiclib.BasicLibComponant
import xianchao.com.basiclib.utils.BundleUtils
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.LibViewUtils
import xianchao.com.basiclib.utils.checkNotEmpty
import xianchao.com.basiclib.widget.BgViewPager
import java.math.BigDecimal
import kotlin.concurrent.thread

class HomeFragment : BaseFragment() {

    var assertsAdapter: HomeAssertsAdapter? = null
    lateinit var homeCardAdatper: HomeCardAdatper
    lateinit var vpContainer: BgViewPager

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }


    var pageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(p0: Int) {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }

        override fun onPageSelected(index: Int) {
            thread {
                loadData()
            }
//            loadData()
//            BasicLibComponant.postMainDelay(object : Runnable {
//                override fun run() {
//                    loadData()
//                }
//            }, 500)
//            EventBus.getDefault().post(ETHNoteChangeEvent())
        }

    }


    fun changeWallet(wallet: GreWalletModel) {
        var assets = LiteCoinBeanOperator.findAllFromParent(wallet.address)
        assertsAdapter?.setNewData(assets)
        queryFbcAssetBalance(assets, wallet)
    }

    // 获取到所有 coinbean 下边的余额计算总和
    fun updataCardBalance(assets: MutableList<LiteCoinBeanModel>, wallet: GreWalletModel) {
//            wallet.balance
        val data = assertsAdapter!!.data
        if (CheckedUtils.isEmpty(data)) {
            wallet.rmb = ""
            homeCardAdatper.notifyDataSetChanged()
            return@updataCardBalance
        }
        var allBalance = BigDecimal.ZERO
        for (datum in assets) {
            if ("CWV".equals(datum.coin_symbol)) {
                allBalance = BigDecimal(MoneyUtils.commonHandleDecimal(datum?.count))
            }
        }
        wallet.rmb = allBalance.toString()
        assertsAdapter!!.greWalletModel = wallet
        assertsAdapter!!.notifyDataSetChanged()
//            homeCardAdatper.notifyDataSetChanged()
        homeCardAdatper.notifyItemDataChanged(homeCardAdatper.allWallet.indexOf(wallet))
    }

    /**
     * 获取余额
     */
    private fun queryFbcAssetBalance(assets: MutableList<LiteCoinBeanModel>, wallet: GreWalletModel) {


        balancePresenter.getAddressBalance(wallet.address) { resCode, balanceValue ->
            if ("1".equals(resCode)) {
                assets.forEach {
                    val realSymbol = AppUtils.getRealSymbol(it.coin_symbol)
                    val balanceToken = balanceValue?.tokensMap?.get(realSymbol)
                    it!!.count = MoneyUtils.commonHandleDecimal(balanceToken?.balance)
                    it?.countCNY = "0.00"
                }

                // 更新card
                updataCardBalance(assets, wallet)
            }
        }
    }

    lateinit var presenter: TransferFbcPresenter
    lateinit var balancePresenter: BalancePresenter
    lateinit var nodePresenter: NodePresenter

    override fun initView() {
        balancePresenter = BalancePresenter()
        nodePresenter = NodePresenter()

        EventBus.getDefault().register(this)
        refreshLayout.setEnableLoadmore(false)
        refreshLayout.setOnRefreshListener {

            if (!AppUtils.isNetworkConnected()) {
                Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show()
            } else {
                loadData()
            }
            refreshLayout.finishRefresh(800)
        }

        tv_wallet.setOnClickListener(this)
        tv_addmore.setOnClickListener(this)

        assertsAdapter = HomeAssertsAdapter(R.layout.item_home_assert)

        rl_recycler.layoutManager = LinearLayoutManager(activity)
        rl_recycler.adapter = assertsAdapter
        assertsAdapter!!.addHeaderView(initRefreshLayoutHeader())
        if (homeCardAdatper.allWallet.checkNotEmpty()) {
            assertsAdapter?.greWalletModel = homeCardAdatper.allWallet[0]
        }

        assertsAdapter!!.onItemClickListener = object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

                if (LibViewUtils.isSharkClick(view!!)) {
                    return
                }
                if (!isInitNodeSuccess()) {
                    return
                }
                var bundle = BundleUtils.createWith(PageParamter.PAREMTER_LITE_COINBEAN
                        , adapter!!.data[position] as LiteCoinBeanModel)
                startActivity(WalletDetailActivity::class.java, bundle)
            }

        }

        loadData()
    }

    private fun initRefreshLayoutHeader(): View? {
        if (refreshLayout == null) {
            return null
        }
        val headerView = layoutInflater.inflate(R.layout.header_home_fragment, null)
        vpContainer = headerView.findViewById<BgViewPager>(R.id.vp_container)
        var iv_add_assets = headerView.findViewById<ImageView>(R.id.iv_add_assets)

        iv_add_assets.setOnClickListener(this)
        vpContainer.setPageTransformer(true, ZoomOutPageTransformer())
        vpContainer.offscreenPageLimit = 3

        homeCardAdatper = HomeCardAdatper(activity as BaseActivity)
        homeCardAdatper.allWallet = GreWalletOperator.queryAll()
        vpContainer.adapter = homeCardAdatper
        vpContainer.setOnPageChangeListener(pageChangeListener)
//
        return headerView
    }

    fun isInitNodeSuccess(): Boolean {
        if (!nodePresenter.isInitNodeSuccess()) {
            if (!AppUtils.isNetworkConnected()) {
                showTopMsg("请检查网络")
                return false
            }
            showProgressDialog("正在重新加载主链节点...")
            nodePresenter.initCWVnode {
                hideProgressDialog()
                if ("1".equals(it)) {
                    showTopMsg("节点初始化成功")
                } else {
                    showTopMsg("节点初始化失败，请检查网络")
                }
            }
            return false
        }
        return true
    }

    override fun onClick(v: View) {
        if (LibViewUtils.isSharkClick(v)) {
            SWLog.e("重复点击。。。。。")
            return
        }

        if (!isInitNodeSuccess()) {
            return
        }


        when (v.id) {
            R.id.tv_wallet -> {
                startActivity(AllWalletActivity::class.java)
            }
            R.id.tv_addmore -> {
                startActivity(ImportWalletTypeActivity::class.java)
            }
            R.id.iv_add_assets -> {

                var wallet = homeCardAdatper.allWallet[vpContainer.currentItem % homeCardAdatper.allWallet.size]
                val bundle = Bundle()
                bundle.putSerializable(PageParamter.PAREMTER_WALLET, wallet)
                startActivity(AddAssetActivity::class.java, bundle)
            }
        }
    }

    override fun loadData() {
        SWLog.e("loadData:::loadDataloadDataloadData")
        if (homeCardAdatper.allWallet.size - 1 > vpContainer.currentItem) {
            val walletModel = homeCardAdatper.allWallet[vpContainer.currentItem]
            changeWallet(walletModel)
        } else {
            val walletModel = homeCardAdatper.allWallet[homeCardAdatper.allWallet.size - 1]
            changeWallet(walletModel)
        }

    }


    var hidden: Boolean = false

    override fun onResume() {
        super.onResume()
        if (!AppUtils.isNetworkConnected()) {
            Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show()
        } else {
            if (!hidden) {
                loadData()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        this.hidden = hidden
        if (!hidden) {
            loadData()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun walletChange(walletChangeEvent: WalletChangeEvent) {

        homeCardAdatper.allWallet = GreWalletOperator.queryAll()
        if (homeCardAdatper.allWallet.size - 1 > vpContainer.currentItem) {
            vpContainer.adapter = homeCardAdatper
            vpContainer.setCurrentItem(vpContainer.currentItem)
        } else {
            vpContainer.adapter = homeCardAdatper
            vpContainer.setCurrentItem(homeCardAdatper.allWallet.size - 1)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ethNodeChange(ethEvent: ETHNoteChangeEvent) {
        loadData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun cwvNodeChange(cwvEvent: CWVNoteChangeEvent) {
        loadData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(cwvEvent: HomeShowWalletEvent) {
        if (cwvEvent.walletModel.address.equals(assertsAdapter?.greWalletModel?.address)) {
            assertsAdapter!!.greWalletModel = cwvEvent.walletModel
            assertsAdapter!!.notifyDataSetChanged()
        }
    }

}







