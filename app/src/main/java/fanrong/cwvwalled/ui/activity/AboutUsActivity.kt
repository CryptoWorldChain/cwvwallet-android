package fanrong.cwvwalled.ui.activity

import android.app.Dialog
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.http.engine.ConvertToBody
import fanrong.cwvwalled.http.engine.RetrofitClient
import fanrong.cwvwalled.http.model.MarketInfoResp
import fanrong.cwvwalled.http.model.UpdateResp
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.layout_title.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.ui.view.UpdateDialog


class AboutUsActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_about_us
    }

    override fun initView() {
        toolbar_title.text = "关于我们"
        iv_left_image.setOnClickListener { finish() }
        tv_version.text = "V " + packageManager.getPackageInfo(packageName, 0).packageName
    }

    override fun onClick(v: View) {

    }

    override fun loadData() {

    }

}