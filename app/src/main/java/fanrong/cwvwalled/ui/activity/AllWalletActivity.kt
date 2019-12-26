package fanrong.cwvwalled.ui.activity

/*
* Wallet
*
* */
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.yanzhenjie.recyclerview.swipe.*
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.eventbus.HomeCardNumChangeEvent
import fanrong.cwvwalled.eventbus.WalletChangeEvent
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.litepal.LiteCoinBeanOperator
import fanrong.cwvwalled.ui.adapter.WalletAdapter
import fanrong.cwvwalled.utils.DensityUtil
import kotlinx.android.synthetic.main.activity_all_wallet.*
import kotlinx.android.synthetic.main.item_all_wallet.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal

class AllWalletActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var queryCWV: GreWalletModel? = null
    var queryETH: GreWalletModel? = null
    lateinit var walletAdapter: WalletAdapter


    override fun getLayoutId(): Int {
        return R.layout.activity_all_wallet
    }

    override fun initView() {
        EventBus.getDefault().register(this)

        bgll_cwv_wallet.setOnClickListener(this)
        //  bgll_eth_wallet.setOnClickListener(this)

        setTitleText("Wallet")
        setLeftImgOnclickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        queryCWV = GreWalletOperator.queryMainCWV()
        tv_cwv_wallet_name.text = queryCWV?.walletName
        tv_cwv_address.text = queryCWV?.address

//        queryETH = GreWalletOperator.queryMainETH()
//        tv_eth_wallet_name.text = queryETH?.walletName
//        tv_eth_address.text = queryETH?.address

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_recyclerview.layoutManager = linearLayoutManager
        walletAdapter = WalletAdapter(R.layout.item_all_wallet)

        rv_recyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER)
        rv_recyclerview.setSwipeMenuCreator(menuCreator)
        rv_recyclerview.setSwipeItemClickListener(object : SwipeItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable(PageParamter.PAREMTER_WALLET, walletAdapter.datas[position])
                startActivity(WalletSettingActivity::class.java, bundle)
            }

        })
        rv_recyclerview.setSwipeMenuItemClickListener(object : SwipeMenuItemClickListener {
            override fun onItemClick(menuBridge: SwipeMenuBridge, position: Int) {
                menuBridge.closeMenu()
                val greWalletModel = walletAdapter.datas[position]
                greWalletModel.delete()
                LiteCoinBeanOperator.delectAllFromParent(greWalletModel.address)
                walletAdapter.datas.remove(greWalletModel)
                walletAdapter.notifyDataSetChanged()
                EventBus.getDefault().post(WalletChangeEvent())
            }
        })
        rv_recyclerview.adapter = walletAdapter

        loadData()
    }

    var menuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(leftMenu: SwipeMenu?, rightMenu: SwipeMenu, position: Int) {
            val deleteItem = SwipeMenuItem(this@AllWalletActivity)
            deleteItem.text = "删除"
            deleteItem.setTextColor(Color.parseColor("#FFFFFF"))
            deleteItem.height = ViewGroup.LayoutParams.MATCH_PARENT
            deleteItem.width = DensityUtil.dp2px(resources, 80f).toInt()
            deleteItem.setBackgroundColor(Color.parseColor("#D7390D"))
            rightMenu.addMenuItem(deleteItem)
        }
    }

    override fun onRestart() {
        super.onRestart()
        loadData()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bgll_cwv_wallet -> {
                val bundle = Bundle()
                bundle.putSerializable(PageParamter.PAREMTER_WALLET, queryCWV)
                startActivity(WalletSettingActivity::class.java, bundle)

            }
//            R.id.bgll_eth_wallet -> {
//                val bundle = Bundle()
//                bundle.putSerializable(PageParamter.PAREMTER_WALLET, queryETH)
//                startActivity(WalletSettingActivity::class.java, bundle)
//            }
        }
    }

    override fun loadData() {
        val import = GreWalletOperator.queryImport()
        walletAdapter.setNewData(import)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun walletChange(walletChangeEvent: WalletChangeEvent) {
        queryCWV = GreWalletOperator.queryMainCWV()
        tv_cwv_wallet_name.text = queryCWV?.walletName
        tv_cwv_address.text = queryCWV?.walletType
//        queryETH = GreWalletOperator.queryMainETH()
//        tv_eth_wallet_name.text = queryETH?.walletName
//        tv_eth_address.text = queryETH?.walletType

        loadData()
    }

}
