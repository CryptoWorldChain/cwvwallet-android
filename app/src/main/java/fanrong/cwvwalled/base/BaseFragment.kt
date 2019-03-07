package fanrong.cwvwalled.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fanrong.cwvwalled.R

abstract class BaseFragment : Fragment(), View.OnClickListener {

    abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * 方法在baseFragment onViewCreated 里边调用过，实现类不需要自己调用
     */
    abstract fun initView()

    abstract fun loadData()
    fun showProgressDialog(msg: String) {
        (activity as BaseActivity).showProgressDialog(msg)
    }

    fun hideProgressDialog() {
        (activity as BaseActivity).hideProgressDialog()
    }

    fun startActivity(clazz: Class<out Activity>) {
        activity!!.startActivity(Intent(activity, clazz))
        activity!!.overridePendingTransition(R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_left_exit)
    }

    fun startActivity(clazz: Class<out Activity>, bundle: Bundle) {
        val intent = Intent(activity, clazz)
        intent.putExtras(bundle)
        activity!!.startActivity(intent)
        activity!!.overridePendingTransition(R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_left_exit)
    }

    fun showTopMsg(msg: String) {
        var baseActivity = activity as BaseActivity
        baseActivity.showTopMsg(msg)
    }

    fun finishActivityDelay(delay: Long) {
        Handler().postDelayed({ activity!!.finish() }, delay)
    }
}