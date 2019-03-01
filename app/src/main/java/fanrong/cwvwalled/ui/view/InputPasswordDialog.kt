package fanrong.cwvwalled.ui.view

import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import fanrong.cwvwalled.R
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.utils.FanrongViewUtils
import kotlinx.android.synthetic.main.layout_dialog_input_password.*
import net.sourceforge.UI.view.FullScreenHalfUpDialog
import net.sourceforge.listener.TextWatcherAfter
import xianchao.com.basiclib.utils.CheckedUtils

class InputPasswordDialog : FullScreenHalfUpDialog, View.OnClickListener {

    constructor(context: Context) : super(context)


    var btnListener: FRDialogBtnListener? = null

    var inputPassword: String? = null

    override fun getContentView(): Int {
        return R.layout.layout_dialog_input_password
    }

    override fun initView() {
        findViewById<Button>(R.id.btn_cancel).setOnClickListener(this)
        findViewById<Button>(R.id.btn_confirm).setOnClickListener(this)
        et_password.addTextChangedListener(object : TextWatcherAfter() {
            override fun afterTextChanged(s: Editable?) {
                FanrongViewUtils.updataDialogBtn(btn_confirm, CheckedUtils.nonEmpty(s.toString()))
            }
        })
    }


    override fun onClick(v: View) {
        if (btnListener == null) {
            return
        }

        when (v.id) {
            R.id.btn_cancel -> {
                btnListener?.onCancel(this)
            }
            R.id.btn_confirm -> {
                inputPassword = et_password.text.toString()
                btnListener?.onConfirm(this)
            }
        }

    }


}