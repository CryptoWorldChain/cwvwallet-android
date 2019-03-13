package fanrong.cwvwalled.ui.view

import android.content.Context
import android.view.View
import fanrong.cwvwalled.R
import fanrong.cwvwalled.listener.FRDialogBtnListener
import kotlinx.android.synthetic.main.layout_dialog_exit.*
import net.sourceforge.UI.view.FullScreenDialog

class PasswordHintDialog : FullScreenDialog, View.OnClickListener {
    constructor(context: Context) : super(context)


    var btnlistener: FRDialogBtnListener? = null

    override fun getContentView(): Int {
        return R.layout.layout_dialog_password_hint
    }

    override fun initView() {
        btn_confirm.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        if (btnlistener == null) {
            return
        }

        when (v.id) {
            R.id.btn_confirm -> {
                btnlistener!!.onConfirm(this)
            }
        }
    }
}