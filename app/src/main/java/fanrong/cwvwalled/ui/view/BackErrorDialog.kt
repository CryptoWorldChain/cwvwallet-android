package net.sourceforge.UI.view

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import fanrong.cwvwalled.R

class BackErrorDialog : FullScreenDialog {
    override fun getContentView(): Int {
        return R.layout.layout_dialog_transfer_error
    }

    override fun initView() {
        findViewById<TextView>(R.id.tv_status).setText("恢复失败")
        findViewById<TextView>(R.id.tv_msg).setText("请认真检查助记词是否正确")
    }

    constructor(context: Context) : super(context) {

    }
}