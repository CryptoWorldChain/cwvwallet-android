package fanrong.cwvwalled.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.common.PageParamter
import kotlinx.android.synthetic.main.activity_import_wallet_type.*

class ImportWalletTypeActivity : BaseActivity() {

    override fun initView() {
        ll_cwv_type.setOnClickListener(this)
      //  ll_eth_type.setOnClickListener(this)

        setTitleText("请选择钱包类型")
        setLeftImgOnclickListener(View.OnClickListener { finish() })

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_cwv_type -> {
                val bundle = Bundle()
                bundle.putString(PageParamter.PAREMTER_WALLET_TYPE, "CWV")
                startActivity(ImportWalletActivity::class.java, bundle)
            }
//            R.id.ll_eth_type -> {
//                val bundle = Bundle()
//                bundle.putString(PageParamter.PAREMTER_WALLET_TYPE, "ETH")
//                startActivity(ImportWalletActivity::class.java, bundle)
//            }
        }
    }

    override fun loadData() {
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_import_wallet_type
    }
}
