package xianchao.com.basiclib.utils

import android.app.Dialog
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import xianchao.com.basiclib.R

object LibViewUtils {


    fun setViewBottomMargin(view: View, size: Int) {
        if (view.layoutParams is LinearLayout.LayoutParams) {
            var layoutParams = view.layoutParams as LinearLayout.LayoutParams
            layoutParams.bottomMargin = size
            view.layoutParams = layoutParams
        } else if (view.layoutParams is RelativeLayout.LayoutParams) {
            var layoutParams = view.layoutParams as LinearLayout.LayoutParams
            layoutParams.bottomMargin = size
            view.layoutParams = layoutParams
        }

    }

    fun isNoMoreData(pageNo: Int, pageSize: Int, totalData: Int): Boolean {
        return pageNo * pageSize > totalData
    }

    fun isSharkClick(v: View?): Boolean {
        if (v == null) {
            return false
        }
        val l = if (v!!.getTag(R.id.basiclib_last_click_time) == null) {
            0L
        } else {
            v!!.getTag(R.id.basiclib_last_click_time) as Long
        }
        v.setTag(R.id.basiclib_last_click_time, System.currentTimeMillis())
        return (System.currentTimeMillis() - l < 700)
    }

    open class TextWatcherAfter : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    }

    fun addTextsWatcher(textWatcher: TextWatcher, vararg editTexts: EditText) {

        if (!(editTexts == null || editTexts.isEmpty())) {
            for (editText in editTexts) {
                editText.addTextChangedListener(textWatcher)
            }
        }
    }

    /**
     * 监听所有输入框，当所有输入框都不为空的情况下 button 激活，editText 可以传多个
     */
    fun updateBtnStatus(button: Button, vararg editTexts: EditText) {
        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcherAfter() {
                override fun afterTextChanged(s: Editable?) {
                    button.isEnabled = notEmptyContent(*editTexts)
                }
            })
        }
        if (editTexts == null) {
            return
        }
    }

    /**
     * 判断输入框是否为空，可以传多个
     */
    fun notEmptyContent(vararg editTexts: EditText): Boolean {
        if (editTexts.size == 0) {
            return false
        }
        for (editText in editTexts) {
            if (editText.text.toString().checkIsEmpty()) {
                return false
            }
        }
        return true
    }


    fun changeLineBg(editText: EditText, view: View) {
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                view.setBackgroundColor(Color.parseColor("#FFF6222E"))
            } else {
                view.setBackgroundColor(Color.parseColor("#14000000"))
            }
        }

    }

}