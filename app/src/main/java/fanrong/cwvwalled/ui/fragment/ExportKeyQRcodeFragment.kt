package fanrong.cwvwalled.ui.fragment

import android.view.View
import com.yzq.zxinglibrary.encode.CodeCreator
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.BaseFragment
import fanrong.cwvwalled.common.PageParamter
import fanrong.cwvwalled.litepal.GreWalletModel
import kotlinx.android.synthetic.main.fragment_export_key_qrcode.*

class ExportKeyQRcodeFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_export_key_qrcode
    }

    lateinit var wallet: GreWalletModel

    override fun initView() {
        wallet = arguments!!.getSerializable(PageParamter.PAREMTER_WALLET) as GreWalletModel
        btn_show.setOnClickListener(this)
        iv_qrcode.setImageBitmap(CodeCreator.createQRCode(wallet.address, 400, 400, null))
    }

    override fun loadData() {
    }

    override fun onClick(v: View?) {
        iv_cover.visibility = View.GONE
        btn_show.visibility = View.GONE
    }

}