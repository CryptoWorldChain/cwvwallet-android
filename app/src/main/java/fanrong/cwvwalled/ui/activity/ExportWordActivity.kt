package fanrong.cwvwalled.ui.activity

import android.os.Bundle
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.utils.AppUtils
import kotlinx.android.synthetic.main.activity_export_word.*

class ExportWordActivity : BaseActivity() {

    override fun initView() {
        wallet = intent.getSerializableExtra(PageParamter.PAREMTER_WALLET) as GreWalletModel

        btn_copy.setOnClickListener(this)

        setTitleText("导出助记词")
        setLeftImgOnclickListener(View.OnClickListener { finish() })
        tv_mnemic.text = wallet.mnemonic

    }

    override fun onClick(v: View) {
        AppUtils.clipboardString(this, tv_mnemic.text.toString())
        showTopMsg("已复制")
    }

    override fun loadData() {
    }

    lateinit var wallet: GreWalletModel


    override fun getLayoutId(): Int {
        return R.layout.activity_export_word
    }
}
