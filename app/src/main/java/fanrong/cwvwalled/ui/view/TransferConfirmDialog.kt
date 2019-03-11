package fanrong.cwvwalled.ui.view

import android.content.Context
import fanrong.cwvwalled.R
import fanrong.cwvwalled.listener.FRDialogBtnListener
import kotlinx.android.synthetic.main.layout_dialog_transfer_confrim.*
import net.sourceforge.UI.view.FullScreenDialog

class TransferConfirmDialog : FullScreenDialog {

    constructor(context: Context) : super(context)

    var listener: FRDialogBtnListener? = null

    var toAddress: String? = null
    var fromAddress: String? = null
    var gas: String? = null
    var count: String? = null
    var remark: String? = null

    override fun getContentView(): Int {
        return R.layout.layout_dialog_transfer_confrim
    }

    override fun initView() {

        btn_confirm.setOnClickListener {
            if (listener != null) {
                listener!!.onConfirm(this@TransferConfirmDialog)
            }
        }

        tv_to_address.text = toAddress
        tv_from_address.text = fromAddress
        tv_count.text = count
        tv_gas.text = gas
        tv_remark.text = remark

    }
}