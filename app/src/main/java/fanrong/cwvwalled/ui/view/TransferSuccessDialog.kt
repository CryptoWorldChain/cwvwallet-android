package fanrong.cwvwalled.ui.view

import android.content.Context
import fanrong.cwvwalled.R
import net.sourceforge.UI.view.FullScreenDialog

class TransferSuccessDialog :FullScreenDialog{
    constructor(context: Context) : super(context)


    override fun getContentView(): Int {
        return R.layout.layout_dialog_transfer_success
    }

    override fun initView() {

    }
}