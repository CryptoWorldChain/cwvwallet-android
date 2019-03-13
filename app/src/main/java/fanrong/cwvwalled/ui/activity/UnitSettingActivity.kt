package fanrong.cwvwalled.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity

class UnitSettingActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_language_setting
    }

    override fun initView() {
        setLeftImgOnclickListener { finish() }
        setTitleText("货币单位")
    }

    override fun onClick(v: View) {
    }

    override fun loadData() {
    }
}
