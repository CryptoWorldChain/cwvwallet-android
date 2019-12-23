package fanrong.cwvwalled.ui.activity

import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.ValueCallBack
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.litepal.LiteCoinBeanModel
import fanrong.cwvwalled.litepal.LiteCoinBeanOperator
import fanrong.cwvwalled.litepal.TokenInfo
import fanrong.cwvwalled.parenter.AddAssetCWVPresenter
import fanrong.cwvwalled.parenter.AddAssetPresenter
import fanrong.cwvwalled.ui.adapter.AddAssetAdapter
import kotlinx.android.synthetic.main.activity_add_asset.*
import net.sourceforge.http.model.CWVCoinType
import net.sourceforge.listener.TextWatcherAfter

class AddAssetActivity : BaseActivity() {

    var inputStr: String = ""
    var adapter: AddAssetAdapter? = null

    var handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            requestAsset()
        }
    }

    lateinit var presenter: AddAssetPresenter
    lateinit var wallet: GreWalletModel


    override fun getLayoutId(): Int {
        return R.layout.activity_add_asset
    }

    override fun initView() {
        wallet = intent.getSerializableExtra(PageParamter.PAREMTER_WALLET) as GreWalletModel
        iv_back.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
//        if ("ETH".equals(wallet.walletType)) {
//          //  presenter = AddAssetETHPresenter()
//        } else {
            presenter = AddAssetCWVPresenter()
  //      }
        presenter.hasCoins.clear()
//        presenter.hasCoins.addAll(LiteCoinBeanOperator.findAllFromParent(wallet.address))
        et_search.addTextChangedListener(object : TextWatcherAfter() {
            override fun afterTextChanged(s: Editable?) {
                inputStr = s.toString()
                handler.removeMessages(1)
                handler.sendEmptyMessageDelayed(1, 1000)
            }
        })

        rv_recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = AddAssetAdapter()
        rv_recyclerview.adapter = adapter
        adapter!!.checkedChangeListener = object : AddAssetAdapter.OnItemCheckedChangeListener {
            override fun onItemChange(coinBean: TokenInfo, isChecked: Boolean) {
             //   coinBean.tokenAddress
                coinBean.sourceAddr =wallet.address
                presenter.changeAssetStatus(coinBean, isChecked)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.tv_cancel -> {
                et_search.setText("")
            }
        }
    }


    override fun loadData() {
        requestAsset()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun requestAsset() {

        presenter.requestAsset(inputStr, object : ValueCallBack<List<TokenInfo>> {
            override fun valueBack(coinbeans: List<TokenInfo>) {
                adapter?.coinBeans = coinbeans
            }
        },wallet)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeMessages(1)
    }
}
