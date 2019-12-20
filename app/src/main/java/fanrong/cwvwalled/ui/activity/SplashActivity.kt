package fanrong.cwvwalled.ui.activity

import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.UserInfoObject
import fanrong.cwvwalled.litepal.GreNodeOperator
import fanrong.cwvwalled.litepal.GreWalletOperator
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.NodeListReq
import fanrong.cwvwalled.http.model.NodeListResp
import fanrong.cwvwalled.litepal.GreNodeModel
import fanrong.cwvwalled.parenter.NodePresenter
import fanrong.cwvwalled.utils.BgUtils
import fanrong.cwvwalled.utils.SWLog
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xianchao.com.basiclib.utils.CheckedUtils

class SplashActivity : BaseActivity() {

    lateinit var presenter: NodePresenter

    override fun loadData() {
        // 初始化node节点
        presenter.initCWVnode()
    }

    override fun onClick(v: View) {
        if (checkExistWallet()) {
            startActivity(MainActivity::class.java)
        } else {
            startActivity(CreateAccountPreActivity::class.java)
        }
        finish()

    }


    override fun initView() {
        presenter = NodePresenter()
        contentView.setBackgroundResource(BgUtils.getBg(BgUtils.YINDAO))
        iv_tonext.setOnClickListener(this)
        UserInfoObject.init()
        loadData()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }


    private fun checkExistWallet(): Boolean {
        if (CheckedUtils.nonEmpty(GreWalletOperator.queryAll())) {
            return true
        }
        return false
    }

}
