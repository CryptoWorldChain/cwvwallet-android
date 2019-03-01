package fanrong.cwvwalled.ui.view

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.utils.FanrongViewUtils
import kotlinx.android.synthetic.main.layout_dialog_input_password.*
import net.sourceforge.UI.view.FullScreenHalfUpDialog
import net.sourceforge.listener.TextWatcherAfter
import xianchao.com.basiclib.utils.CheckedUtils

class ChangeWalletNameDialog : FullScreenHalfUpDialog, View.OnClickListener {

    constructor(context: Context) : super(context)


    var btnListener: FRDialogBtnListener? = null

    var walletName: String? = null

    override fun getContentView(): Int {
        return R.layout.layout_dialog_input_password
    }

    override fun initView() {
        findViewById<Button>(R.id.btn_cancel).setOnClickListener(this)
        findViewById<Button>(R.id.btn_confirm).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_title).text = "更改钱包名称"
        et_password.inputType = InputType.TYPE_CLASS_TEXT
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
                walletName = et_password.text.toString()
                btnListener?.onConfirm(this)
            }
        }

    }


}