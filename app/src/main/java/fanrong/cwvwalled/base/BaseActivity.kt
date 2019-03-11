package fanrong.cwvwalled.base

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import fanrong.cwvwalled.R
import fanrong.cwvwalled.ui.view.MsgPopupWindow
import fanrong.cwvwalled.ui.view.WheelDialog

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {

    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract override fun onClick(v: View)
    abstract fun loadData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = Color.TRANSPARENT
            }
        }
        setContentView(getLayoutId())
        initView()
    }


    fun setTitleText(title: String) {
        findViewById<TextView>(R.id.toolbar_title).text = title
    }

    fun setLeftImgOnclickListener(onClickListener: View.OnClickListener) {
        findViewById<ImageView>(R.id.iv_left_image).setOnClickListener(onClickListener)
    }

    fun setLeftImgOnclickListener(callback: () -> Unit) {

        findViewById<ImageView>(R.id.iv_left_image).setOnClickListener { callback() }
    }

    fun setRightImgOnclickListener(resourceId: Int, callback: () -> Unit) {

        findViewById<ImageView>(R.id.iv_right_image).setImageResource(resourceId)
        findViewById<ImageView>(R.id.iv_right_image).setOnClickListener { callback() }
    }


    fun startActivity(clazz: Class<out Activity>) {
        startActivity(Intent(this, clazz))
        overridePendingTransition(R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_left_exit)
    }

    fun startActivity(clazz: Class<out Activity>, bundle: Bundle) {
        var intent = Intent(this, clazz)
        intent.putExtras(bundle)
        startActivity(intent)
        overridePendingTransition(R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_left_exit)
    }


    fun startActivityForResult(clazz: Class<out Activity>, bundle: Bundle) {
        var intent = Intent(this, clazz)
        intent.putExtras(bundle)
        startActivityForResult(intent, 0)
        overridePendingTransition(R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_left_exit)
    }

    fun startActivityForResult(clazz: Class<out Activity>, bundle: Bundle, requestCode: Int) {
        var intent = Intent(this, clazz)
        intent.putExtras(bundle)
        startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_left_exit)
    }


    fun showTopMsg(msg: String) {
        val viewById1 = window.decorView.findViewById<ViewGroup>(android.R.id.content)
        val popupwindow = MsgPopupWindow.getPopupwindow()
        val viewById = popupwindow.getContentView().findViewById<TextView>(R.id.tv_msg)
        viewById.setText(msg)
        popupwindow.showAtLocation(viewById1.getChildAt(0), Gravity.NO_GRAVITY, 0, 0)


        for (callback in callbacks) {
            dismissHandler.removeCallbacks(callback)
        }
        callbacks.clear()
        var callback = Runnable { popupwindow.dismiss() }
        callbacks.add(callback)
        dismissHandler.postDelayed(callback, 1500)

    }

    private var dismissHandler = Handler()
    private var callbacks = mutableListOf<Runnable>()

    fun showTopMsgWithDialog(msg: String, dialog: Dialog) {

        val viewById1 = dialog.window!!.decorView
        val popupwindow = MsgPopupWindow.getPopupwindow()
        val viewById = popupwindow.getContentView().findViewById<TextView>(R.id.tv_msg)
        viewById.setText(msg)
        popupwindow.showAtLocation(viewById1, Gravity.NO_GRAVITY, 0, 0)

        for (callback in callbacks) {
            dismissHandler.removeCallbacks(callback)
        }
        callbacks.clear()
        var callback = Runnable { popupwindow.dismiss() }
        callbacks.add(callback)
        dismissHandler.postDelayed(callback, 1500)
    }


    private var mWheelDialog: WheelDialog? = null

    fun showProgressDialog(message: String) {
        if (mWheelDialog == null) {
            mWheelDialog = WheelDialog(this, R.style.WheelDialog)
        }
        if (mWheelDialog!!.isShowing()) {
            return
        }
        if (TextUtils.isEmpty(message)) {
            mWheelDialog!!.setMessage("请稍后..")
        } else {
            mWheelDialog!!.setMessage(message)
        }
        mWheelDialog!!.show()
    }

    fun hideProgressDialog() {
        if (mWheelDialog != null && mWheelDialog!!.isShowing()) {
            mWheelDialog!!.dismiss()
        }
    }


    override fun finish() {
        super.finish()

        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out)
    }

}