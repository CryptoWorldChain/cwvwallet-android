package fanrong.cwvwalled.ui.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import fanrong.cwvwalled.R
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.eventbus.CWVNoteChangeEvent
import fanrong.cwvwalled.eventbus.ETHNoteChangeEvent
import fanrong.cwvwalled.eventbus.WalletChangeEvent
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.GetBalanceReq
import fanrong.cwvwalled.http.model.WalletBalanceModel
import fanrong.cwvwalled.litepal.*
import fanrong.cwvwalled.parenter.ToRMBPresenter
import fanrong.cwvwalled.parenter.TransferFbcPresenter
import fanrong.cwvwalled.ui.activity.AddAssetActivity
import fanrong.cwvwalled.ui.activity.AllWalletActivity
import fanrong.cwvwalled.ui.activity.ImportWalletTypeActivity
import fanrong.cwvwalled.ui.activity.WalletDetailActivity
import fanrong.cwvwalled.ui.adapter.HomeAssertsAdapter
import fanrong.cwvwalled.ui.adapter.HomeCardAdatper
import fanrong.cwvwalled.ui.view.ZoomOutPageTransformer
import fanrong.cwvwalled.utils.AppUtils
import fanrong.cwvwalled.utils.MoneyUtils
import fanrong.cwvwalled.utils.SWLog
import kotlinx.android.synthetic.main.fragment_home.*
import net.sourceforge.http.model.CWVCoinType
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.BundleUtils
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.checkIsEmpty
import java.math.BigDecimal

class HomeFragment : BaseFragment() {

    var assertsAdapter: HomeAssertsAdapter? = null
    lateinit var homeCardAdatper: HomeCardAdatper

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }


    var pageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(p0: Int) {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }

        override fun onPageSelected(index: Int) {
            loadData()

        }

    }


    fun changeWallet(wallet: GreWalletModel) {
        var assets = LiteCoinBeanOperator.findAllFromParent(wallet.address)
        assertsAdapter?.setNewData(assets)

//        if ("ETH".equals(wallet.walletType)) {
//            tv_asset.text = "ETH Asset"
//            queryEthAssetBalance(assets, wallet)
//        } else {
            tv_asset.text = "CWV Asset"
            queryFbcAssetBalance(assets, wallet)
 //       }
    }

    private fun queryEthAssetBalance(assets: MutableList<LiteCoinBeanModel>, wallet: GreWalletModel) {

        // 获取到所有 coinbean 下边的余额计算总和
        fun updataCardBalance() {
//            wallet.balance
            val data = assertsAdapter!!.data
            if (CheckedUtils.isEmpty(data)) {
                wallet.rmb = ""
                homeCardAdatper.notifyDataSetChanged()
                return@updataCardBalance
            }
            var allBalance = BigDecimal.ZERO
            for (datum in assets) {
                if (CheckedUtils.nonEmpty(datum.countCNY)) {
                    allBalance = allBalance.add(BigDecimal(datum.countCNY))
                }
            }
            wallet.rmb = allBalance.toString()
            assertsAdapter!!.notifyDataSetChanged()
            homeCardAdatper.notifyItemDataChanged(vp_container.currentItem)
        }

        var allItem = mutableListOf<String>()

        for (asset in assets) {
            allItem.add(asset.coin_symbol!!)
            val req = GetBalanceReq()
            req.dapp_id = Constants.DAPP_ID
            req.node_url = GreNodeOperator.queryETHnode().node_url
            req.address = asset.sourceAddr
            req.contract_addr = asset.contract_addr
            RetrofitClient.getETHNetWorkApi()
                    .ethGetBalance(ConvertToBody.ConvertToBody(req))
                    .enqueue(object : Callback<WalletBalanceModel> {
                        override fun onResponse(call: Call<WalletBalanceModel>, response: Response<WalletBalanceModel>) {
                            val body = response.body()
                            val rightNum = MoneyUtils.getRightNum(body!!.balance)
                            asset.count = MoneyUtils.commonHandleDecimal(rightNum)

                            var coin_symbol = asset.coin_symbol!!.replace("(e)", "")
                            coin_symbol = coin_symbol.replace("(c)", "")


                            if (BigDecimal(asset.count).compareTo(BigDecimal.ZERO) == 0) {
                                allItem.remove(asset.coin_symbol!!)
                                if (allItem.checkIsEmpty()) {
                                    // 更新card
                                    updataCardBalance()
                                }
                            } else {

                                ToRMBPresenter.toRMB(asset.count!!, coin_symbol) {

                                    asset.countCNY = MoneyUtils.commonRMBDecimal(it)

                                    allItem.remove(asset.coin_symbol!!)
                                    if (allItem.checkIsEmpty()) {
                                        // 更新card
                                        updataCardBalance()
                                    }
                                }
                            }


                        }

                        override fun onFailure(call: Call<WalletBalanceModel>, t: Throwable) {

                        }
                    })
        }
    }

    private fun queryFbcAssetBalance(assets: MutableList<TokenInfo>, wallet: GreWalletModel) {


        // 获取到所有 coinbean 下边的余额计算总和
        fun updataCardBalance() {
//            wallet.balance
            val data = assertsAdapter!!.data
            if (CheckedUtils.isEmpty(data)) {
                wallet.rmb = ""
                homeCardAdatper.notifyDataSetChanged()
                return@updataCardBalance
            }
            var allBalance = BigDecimal.ZERO
//            for (datum in assets) {
//                if (CheckedUtils.nonEmpty(datum.countCNY)) {
//                    allBalance = allBalance.add(BigDecimal(datum.countCNY))
//                }
//            }
            wallet.rmb = allBalance.toString()
            assertsAdapter!!.notifyDataSetChanged()
            homeCardAdatper.notifyDataSetChanged()
        }


//        for (asset in assets) {
//            val req = GetBalanceReq()
//            req.dapp_id = Constants.DAPP_ID
//            req.node_url = GreNodeOperator.queryCWVnode().node_url
//            req.address = asset.sourceAddr
//            req.contract_addr = asset.contract_addr
//            RetrofitClient.getFBCNetWorkApi()
//                    .fbcGetBalance(ConvertToBody.ConvertToBody(req))
//                    .enqueue(object : Callback<WalletBalanceModel> {
//                        override fun onResponse(call: Call<WalletBalanceModel>, response: Response<WalletBalanceModel>) {
//                            val body = response.body()
//                            val rightNum = MoneyUtils.getRightNum(body!!.balance)
//                            asset.count = MoneyUtils.commonHandleDecimal(rightNum)
//
//                            var coin_symbol = AppUtils.getRealSymbol(asset.coin_symbol)
//                            ToRMBPresenter.toRMB(asset.count!!, coin_symbol) {
//                                asset.countCNY = MoneyUtils.commonRMBDecimal(it)
//                                assertsAdapter!!.notifyDataSetChanged()
//
//                                // 更新card
//                                updataCardBalance()
//                            }
//                        }
//
//                        override fun onFailure(call: Call<WalletBalanceModel>, t: Throwable) {
//
//                        }
//                    })
//        }
  }

    lateinit var presenter:TransferFbcPresenter

    override fun initView() {
        EventBus.getDefault().register(this)

        val coinBeanModel = LiteCoinBeanModel("cwv")
        coinBeanModel.sourceAddr = GreWalletOperator.queryMainCWV().address;
        presenter = TransferFbcPresenter(coinBeanModel)

        tv_wallet.setOnClickListener(this)
        tv_addmore.setOnClickListener(this)
        iv_add_assets.setOnClickListener(this)


        vp_container.setPageTransformer(true, ZoomOutPageTransformer())
        vp_container.offscreenPageLimit = 3

        homeCardAdatper = HomeCardAdatper(activity as BaseActivity)
        vp_container.adapter = homeCardAdatper
        var defaultIndex = homeCardAdatper.allWallet.size * 100
        vp_container.setCurrentItem(defaultIndex)
        vp_container.setOnPageChangeListener(pageChangeListener)
//
        assertsAdapter = HomeAssertsAdapter(R.layout.item_home_assert)
        rl_recycler.layoutManager = LinearLayoutManager(activity)
        rl_recycler.adapter = assertsAdapter
        assertsAdapter!!.onItemClickListener = object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
               // TODO()将TokenInfor 转换为LiteCoinBeanModel
        //        var tokenInfo = adapter!!.data[position] as TokenInfo
                var bundle = BundleUtils.createWith(PageParamter.PAREMTER_LITE_COINBEAN
                        , adapter!!.data[position] as LiteCoinBeanModel)
                startActivity(WalletDetailActivity::class.java, bundle)
            }

        }

        loadData()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_wallet -> {
                presenter.getBalance(object :ValueCallBack<String?>{
                    override fun valueBack(t: String?) {
                        SWLog.e(t)
                    }
                })
                startActivity(AllWalletActivity::class.java)
            }
            R.id.tv_addmore -> {
                startActivity(ImportWalletTypeActivity::class.java)
            }
            R.id.iv_add_assets -> {

                var wallet = homeCardAdatper.allWallet[vp_container.currentItem % homeCardAdatper.allWallet.size]
                val bundle = Bundle()
                bundle.putSerializable(PageParamter.PAREMTER_WALLET, wallet)
                startActivity(AddAssetActivity::class.java, bundle)
            }
        }
    }

    override fun loadData() {
        var wallet = homeCardAdatper.allWallet[vp_container.currentItem % homeCardAdatper.allWallet.size]
        changeWallet(wallet)

    }


    var hidden: Boolean = false

    override fun onResume() {
        super.onResume()
        if (!hidden) {
            loadData()
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
        homeCardAdatper.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ethNodeChange(ethEvent: ETHNoteChangeEvent) {
        loadData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun cwvNodeChange(cwvEvent: CWVNoteChangeEvent) {
        loadData()
    }


}







