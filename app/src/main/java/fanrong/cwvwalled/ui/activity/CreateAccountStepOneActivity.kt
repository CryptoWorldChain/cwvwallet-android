package fanrong.cwvwalled.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.parenter.WalletCreatePresenter
import fanrong.cwvwalled.utils.CallJsCodeUtils
import fanrong.cwvwalled.utils.PreferenceHelper
import fanrong.cwvwalled.utils.SWLog
import kotlinx.android.synthetic.main.activity_create_account_step_one.*
import xianchao.com.basiclib.utils.CheckedUtils
/*
* 助记词展示页面
* */
class CreateAccountStepOneActivity : BaseActivity() {


    var mnemonic = ""
    override fun loadData() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_confirm -> {
                val bundle = Bundle()
                SWLog.e("助记词-》  ${mnemonic}")
                bundle.putString(PageParamter.MNEMONIC_STR, mnemonic)
                startActivity(CreateAccountStepTwoActivity::class.java, bundle)
                toNextPage()
            }
            else -> {

            }
        }
    }

    override fun initView() {
        val presenter = WalletCreatePresenter()
        presenter.getWords {
            mnemonic = it
            tv_mnemic.text = mnemonic.replace(" ", "      ")
        }

        btn_confirm.setOnClickListener(this)
    }



    override fun getLayoutId(): Int {
        return R.layout.activity_create_account_step_one
    }


    private fun toNextPage() {

    }


}
