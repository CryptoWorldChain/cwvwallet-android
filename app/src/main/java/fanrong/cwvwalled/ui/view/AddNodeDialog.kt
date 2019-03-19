package fanrong.cwvwalled.ui.view

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import fanrong.cwvwalled.R
import fanrong.cwvwalled.R.id.btn_confirm
import fanrong.cwvwalled.R.id.et_url
import fanrong.cwvwalled.base.BaseActivity
import fanrong.cwvwalled.listener.FRDialogBtnListener
import kotlinx.android.synthetic.main.layout_dialog_add_node.*
import net.sourceforge.UI.view.FullScreenHalfUpDialog
import net.sourceforge.listener.TextWatcherAfter
import xianchao.com.basiclib.utils.CheckedUtils
import xianchao.com.basiclib.utils.checkIsEmpty

class AddNodeDialog : FullScreenHalfUpDialog, View.OnClickListener {

    constructor(context: Context) : super(context) {
        this.selfContext = context
    }

    override fun getContentView(): Int {
        return R.layout.layout_dialog_add_node
    }


    lateinit var et_url: EditText
    var selfContext: Context

    var node_url: String = ""
    var btnlistener: FRDialogBtnListener? = null


    override fun initView() {


        et_url = findViewById(R.id.et_url)
        val btn_cancel = findViewById<Button>(R.id.btn_cancel)
        val btn_confirm = findViewById<Button>(R.id.btn_confirm)
        btn_cancel.setOnClickListener(this)
        btn_confirm.setOnClickListener(this)
        et_url.setSelection(et_url.text.length)
        if (!node_url.checkIsEmpty()) {
            et_url.setText(node_url)
        }
        et_url.addTextChangedListener(object : TextWatcherAfter() {
            override fun afterTextChanged(s: Editable) {
                btn_confirm.setEnabled(CheckedUtils.nonEmpty(et_url.getText().toString()))
                if (et_url.isEnabled()) {
                    btn_confirm.setTextColor(context.resources.getColor(R.color.dialog_confirm_enable))
                } else {
                    btn_confirm.setTextColor(context.resources.getColor(R.color.dialog_confirm_disable))
                }
            }
        })

    }


    override fun onClick(v: View) {

        when (v.id) {
            R.id.btn_confirm -> {
                if (TextUtils.isEmpty(et_url.text.toString())) {
                    (ownerActivity as BaseActivity).showTopMsgWithDialog("地址不能为空", this)
                }

                node_url = et_url.text.toString()

                if (btnlistener != null) {
                    btnlistener!!.onConfirm(this)
                }
            }
            R.id.btn_cancel -> {
                if (btnlistener != null) {
                    btnlistener!!.onCancel(this)
                }
            }
        }
    }
}