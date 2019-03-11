package fanrong.cwvwalled.ui.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.AppApplication
import fanrong.cwvwalled.listener.FRDialogBtnListener
import fanrong.cwvwalled.utils.FanrongViewUtils
import fanrong.cwvwalled.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_dialog_input_password.*
import net.sourceforge.UI.view.FullScreenHalfUpDialog
import xianchao.com.basiclib.utils.CheckedUtils


class InputAmountDialog : FullScreenHalfUpDialog, View.OnClickListener {

    constructor(context: Context) : super(context)

    override fun getContentView(): Int {
        return R.layout.layout_dialog_input_amount
    }

    var btnListener: FRDialogBtnListener? = null

    var inputCount: String? = null

    override fun initView() {

        var btn_cancel = findViewById<Button>(R.id.btn_cancel)
        var btn_confirm = findViewById<Button>(R.id.btn_confirm)
        tv_coin_symbol = findViewById(R.id.tv_coin_symbol)
        et_count = findViewById(R.id.et_count)

        btn_cancel.setOnClickListener(this)
        btn_confirm.setOnClickListener(this)
        tv_coin_symbol!!.text = coinSymbol
        et_count!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                inputCount = s.toString()
                FanrongViewUtils.updataDialogBtn(btn_confirm, CheckedUtils.nonEmpty(s.toString()))
            }
        })
    }


    var et_count: EditText? = null
    var tv_coin_symbol: TextView? = null
    var coinSymbol: String? = null
        set(value) {
            field = value
            if (tv_coin_symbol != null) {
                tv_coin_symbol!!.text = field
            }
        }

    override fun onClick(v: View) {
        if (btnListener == null) {
            return
        }
        when (v.id) {
            R.id.btn_confirm -> {
                btnListener!!.onConfirm(this)

            }
            R.id.btn_cancel -> {
                btnListener!!.onCancel(this)
            }
        }
    }

}
