package fanrong.cwvwalled.ui.activity

import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import kotlinx.android.synthetic.main.activity_use_setting.*

class UseSettingActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_use_setting
    }

    override fun initView() {

        setLeftImgOnclickListener { finish() }
        setTitleText("使用设置")

        ll_language.setOnClickListener(this)
        ll_coin_unit.setOnClickListener(this)
        ll_node_setting.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_language -> {
                showTopMsg("暂时只支持中文")
            }
            R.id.ll_coin_unit -> {
                startActivity(UnitSettingActivity::class.java)
            }

            R.id.ll_node_setting -> {
                startActivity(NodeConfigActivity::class.java)
            }
        }
    }

    override fun loadData() {
    }
}
