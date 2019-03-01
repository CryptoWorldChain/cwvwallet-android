package fanrong.cwvwalled.ui.activity

import android.os.Bundle
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.database.GreWalletOperator
import fanrong.cwvwalled.utils.BgUtils
import kotlinx.android.synthetic.main.activity_splash.*
import xianchao.com.basiclib.utils.CheckedUtils

class SplashActivity : BaseActivity() {

    override fun loadData() {
    }

    override fun onClick(v: View) {
        if (checkExistWallet()) {
            startActivity(MainActivity::class.java)
        } else {
            startActivity(CreateAccountPreActivity::class.java)
        }

    }

    override fun initView() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        contentView.setBackgroundResource(BgUtils.getBg(BgUtils.YINDAO))
        iv_tonext.setOnClickListener(this)
    }


    private fun checkExistWallet(): Boolean {
        if (CheckedUtils.nonEmpty(GreWalletOperator.queryAll())) {
            return true
        }
        return false
    }
}
