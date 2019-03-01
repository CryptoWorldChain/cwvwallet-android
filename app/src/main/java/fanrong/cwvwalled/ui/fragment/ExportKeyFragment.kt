package fanrong.cwvwalled.ui.fragment

import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.GreWalletModel
import fanrong.cwvwalled.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_export_key.*

class ExportKeyFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_export_key
    }

    lateinit var wallet: GreWalletModel

    override fun initView() {
        wallet = arguments!!.getSerializable(PageParamter.PAREMTER_WALLET) as GreWalletModel
        btn_copy.setOnClickListener(this)
        tv_private_key.text = wallet.privateKey
    }

    override fun loadData() {
    }

    override fun onClick(v: View?) {
        AppUtils.clipboardString(activity, tv_private_key.text.toString())
        showTopMsg("已复制")
    }

}