package fanrong.cwvwalled.ui.view

import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import fanrong.cwvwalled.R
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.utils.FanrongViewUtils
import kotlinx.android.synthetic.main.layout_dialog_nick.*
import net.sourceforge.UI.view.FullScreenHalfUpDialog
import net.sourceforge.listener.TextWatcherAfter
import xianchao.com.basiclib.utils.CheckedUtils

class EditNickNameDialog : FullScreenHalfUpDialog, View.OnClickListener {

    constructor(context: Context) : super(context)

    lateinit var et_nick_name: EditText
    var nickname = ""
    var btnListener: FRDialogBtnListener? = null

    override fun getContentView(): Int {
        return R.layout.layout_dialog_nick
    }

    override fun initView() {

        et_nick_name = findViewById<EditText>(R.id.et_nick_name)
        val btn_cancel = findViewById<Button>(R.id.btn_cancel)
        btn_cancel.setOnClickListener(this)
        val btn_confirm = findViewById<Button>(R.id.btn_confirm)
        btn_confirm.setOnClickListener(this)
        et_nick_name.addTextChangedListener(object : TextWatcherAfter() {
            override fun afterTextChanged(s: Editable) {
                FanrongViewUtils.updataDialogBtn(btn_confirm, CheckedUtils.nonEmpty(et_nick_name.getText().toString()))
            }
        })
    }


    override fun onClick(v: View) {
        if (btnListener == null) {
            return
        }
        when (v.id) {
            R.id.btn_cancel -> {
                btnListener!!.onCancel(this)
            }
            R.id.btn_confirm -> {
                nickname = et_nick_name.text.toString()
                btnListener!!.onConfirm(this)
            }
        }

    }
}