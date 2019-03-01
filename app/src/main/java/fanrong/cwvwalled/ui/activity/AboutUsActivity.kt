package fanrong.cwvwalled.ui.activity

import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import kotlinx.android.synthetic.main.layout_title.*

class AboutUsActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_about_us
    }

    override fun initView() {
        toolbar_title.text="关于我们"
        iv_left_image.setOnClickListener { finish() }
    }

    override fun onClick(v: View) {
    }

    override fun loadData() {
    }
}