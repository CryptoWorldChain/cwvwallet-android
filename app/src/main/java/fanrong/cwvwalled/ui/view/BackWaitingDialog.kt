package net.sourceforge.UI.view

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import fanrong.cwvwalled.R

class BackWaitingDialog : FullScreenDialog {
    override fun getContentView(): Int {
        return R.layout.layout_dialog_transfer_success
    }

    override fun initView() {
        findViewById<ImageView>(R.id.iv_image).setImageResource(R.drawable.recover_loading)
        findViewById<TextView>(R.id.tv_msg).setText("恢复中")
    }

    constructor(context: Context) : super(context) {

    }
}