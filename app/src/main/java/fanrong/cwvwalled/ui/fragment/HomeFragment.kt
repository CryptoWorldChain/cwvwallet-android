package fanrong.cwvwalled.ui.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.base.Constants
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.GetBalanceReq
import fanrong.cwvwalled.http.model.WalletBalanceModel
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.litepal.LiteCoinBeanOperator
import fanrong.cwvwalled.parenter.ToRMBPresenter
import fanrong.cwvwalled.ui.activity.AddAssetActivity
import fanrong.cwvwalled.ui.activity.AllWalletActivity
import fanrong.cwvwalled.ui.activity.ImportWalletTypeActivity
import fanrong.cwvwalled.ui.adapter.HomeAssertsAdapter
import fanrong.cwvwalled.ui.adapter.HomeCardAdatper
import fanrong.cwvwalled.ui.view.ZoomOutPageTransformer
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BaseFragment() {

    var assertsAdapter: HomeAssertsAdapter? = null
    lateinit var homeCardAdatper: HomeCardAdatper
    var cwvAssert = LiteCoinBeanOperator.findAllCWVs()
    var ethAssert = LiteCoinBeanOperator.findAllETHs()

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }


    var pageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(p0: Int) {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }

        override fun onPageSelected(index: Int) {
            val wallet = homeCardAdatper.allWallet[vp_container.currentItem % homeCardAdatper.allWallet.size]
            changeWallet(wallet)
        }

    }


    fun changeWallet(wallet: GreWalletModel) {
        if ("ETH".equals(wallet.walletType)) {
            tv_asset.text = "ETH-Asset"
        } else {
            tv_asset.text = "CWV-Asset"
        }
        GreWalletOperator.queryAll()
        assertsAdapter?.setNewData(LiteCoinBeanOperator.findAllFromParent(wallet.address))
    }

    override fun initView() {
        tv_wallet.setOnClickListener(this)
        tv_addmore.setOnClickListener(this)
        iv_add_assets.setOnClickListener(this)


        vp_container.setPageTransformer(true, ZoomOutPageTransformer())
        vp_container.offscreenPageLimit = 3

        homeCardAdatper = HomeCardAdatper(activity as BaseActivity)
        vp_container.adapter = homeCardAdatper
        var defaultIndex = Integer.MAX_VALUE / 2
        vp_container.setCurrentItem(defaultIndex)
        vp_container.setOnPageChangeListener(pageChangeListener)
//
        assertsAdapter = HomeAssertsAdapter(R.layout.item_home_assert)
        rl_recycler.layoutManager = LinearLayoutManager(activity)
        rl_recycler.adapter = assertsAdapter

        loadData()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_wallet -> {
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
        reqestCWVbalance()
        reqestETHbalance()
    }

    fun reqestCWVbalance() {

        val cwvnode = GreNodeOperator.queryCWVnode()
        val cwvWallet = GreWalletOperator.queryMainCWV()

        val req = GetBalanceReq()
        req.dapp_id = Constants.DAPP_ID
        req.node_url = cwvnode.node_url
        req.address = cwvWallet.address
        req.contract_addr = "CWV"

        RetrofitClient.getFBCNetWorkApi()
                .fbcGetBalance(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<WalletBalanceModel> {

                    override fun onFailure(call: Call<WalletBalanceModel>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<WalletBalanceModel>, response: Response<WalletBalanceModel>) {
                        val body = response.body()!!
//                        homeCardAdatper.getCWVWallet().balance = body.balance
                        ToRMBPresenter.toRMB(body.balance, "CWV") {
                            //                            homeCardAdatper.getCWVWallet().rmb = it
                            homeCardAdatper.notifyDataSetChanged()
                        }
                    }
                })
    }


    fun reqestETHbalance() {
        val gnodeModel = GreNodeOperator.queryETHnode()
        val spdtWallet = GreWalletOperator.queryMainETH()

        val req = GetBalanceReq()
        req.dapp_id = Constants.DAPP_ID
        req.node_url = gnodeModel.node_url
        req.address = spdtWallet.address

        RetrofitClient.getETHNetWorkApi()
                .ethGetBalance(ConvertToBody.ConvertToBody(req))
                .enqueue(object : Callback<WalletBalanceModel> {
                    override fun onFailure(call: Call<WalletBalanceModel>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<WalletBalanceModel>, response: Response<WalletBalanceModel>) {

                        val body = response.body()!!
//                        homeCardAdatper.getCWVWallet().balance = body.balance
                        ToRMBPresenter.toRMB(body.balance, "ETH") {
                            //                            homeCardAdatper.getETHWallet().rmb = it
                            homeCardAdatper.notifyDataSetChanged()
                        }
                    }
                })

    }


    var hidden: Boolean = false

    override fun onResume() {
        super.onResume()
        if (!hidden) {
            loadData()
            changeWallet(homeCardAdatper.allWallet[vp_container.currentItem % homeCardAdatper.allWallet.size])
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        this.hidden = hidden
        if (!hidden) {
            loadData()
        }
    }

}