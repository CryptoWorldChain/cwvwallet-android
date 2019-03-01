package fanrong.cwvwalled.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import fanrong.cwvwalled.R
import fanrong.cwvwalled.ui.view.WheelDialog

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    abstract fun initView()
    abstract override fun onClick(v: View)
    abstract fun loadData()


    fun startActivity(clazz: Class<out Activity>) {
        startActivity(Intent(this, clazz))
    }

    fun startActivity(clazz: Class<out Activity>, bundle: Bundle) {
        var intent = Intent(this, clazz)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    fun showTopMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    lateinit var mWheelDialog: WheelDialog

    fun showProgressDialog(message: String) {
        if (mWheelDialog == null) {
            mWheelDialog = WheelDialog(this, R.style.WheelDialog)
        }
        if (mWheelDialog.isShowing()) {
            return
        }
        if (TextUtils.isEmpty(message)) {
            mWheelDialog.setMessage("请稍后..")
        } else {
            mWheelDialog.setMessage(message)
        }
        mWheelDialog.show()
    }

    fun hideProgressDialog() {
        if (mWheelDialog != null && mWheelDialog.isShowing()) {
            mWheelDialog.dismiss()
        }
    }


}