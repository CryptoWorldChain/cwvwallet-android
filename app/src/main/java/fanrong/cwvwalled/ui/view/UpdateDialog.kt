package fanrong.cwvwalled.ui.view

import android.content.Context
import android.widget.Button
import android.widget.TextView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.listener.FRDialogBtnListener
import net.sourceforge.UI.view.FullScreenDialog

class UpdateDialog : FullScreenDialog {
    constructor(context: Context) : super(context)


    var btnListener: FRDialogBtnListener? = null
    override fun getContentView(): Int {
        return R.layout.layout_dialog_update
    }

    lateinit var version: String

    override fun initView() {
        setCanceledOnTouchOutside(false)
        findViewById<TextView>(R.id.tv_title).text = "发现新版本${version}，立即升级"
        findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            if (btnListener != null) {
                btnListener!!.onConfirm(this)
            }
        }
    }
}