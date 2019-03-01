package fanrong.cwvwalled.ui.view

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import fanrong.cwvwalled.R
import kotlinx.android.synthetic.main.pop_add_address.view.*

class AddAddressDialog(context: Activity) {

    private val mContext: Activity = context
    private var pop: PopupWindow = PopupWindow(context)
    private var pop_parent: View

    private lateinit var onOkListener: OnOkListener

    init {
        val view = context.layoutInflater.inflate(
                R.layout.pop_add_address, null)
        pop_parent = view.pop_parent
        view.iv_add_address_cancel.setOnClickListener {
            pop.dismiss()
        }
        view.tv_address_ok.setOnClickListener {
            onOkListener.OnOk(view.edit_add_address_name.text.toString(),
                    view.edit_add_address_address.text.toString())
            pop.dismiss()
        }
        pop.run {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = ViewGroup.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isFocusable = true
            isOutsideTouchable = true
            contentView = view
        }
        pop.setOnDismissListener {
            val lp = mContext.window.attributes
            lp.alpha = 1f
            mContext.window.attributes = lp
        }
    }


    fun showPop(onOkListener: OnOkListener) {
        this.onOkListener = onOkListener
        pop_parent.startAnimation(AnimationUtils.loadAnimation(
                mContext, R.anim.pop_translate_in))
        pop.showAtLocation(mContext.findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0)
        val lp = mContext.window.attributes
        lp.alpha = 0.5f
        mContext.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        mContext.window.attributes = lp
    }

    /**
     * 和Activity通信的接口
     */
    interface OnOkListener {
        fun OnOk(name: String, address: String)
    }


}