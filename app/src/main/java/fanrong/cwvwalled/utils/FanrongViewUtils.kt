package fanrong.cwvwalled.utils

import android.widget.Button
import android.widget.EditText


import fanrong.cwvwalled.R
import fanrong.cwvwalled.base.AppApplication
import xianchao.com.basiclib.utils.CheckedUtils

/**
 * Created by terry.c on 29/03/2018.
 */

object FanrongViewUtils {


    fun updataBtnStatus(button: Button?, editText: EditText?) {
        if (button == null) {
            return
        }

        if (editText == null) {
            return
        }
        button.isEnabled = CheckedUtils.nonEmpty(editText.text.toString())
        if (button.isEnabled) {
            button.setTextColor(AppApplication.instance.getResources().getColor(R.color.dialog_confirm_enable))
        } else {
            button.setTextColor(AppApplication.instance.getResources().getColor(R.color.dialog_confirm_disable))
        }

    }

    fun updataDialogBtn(button: Button?, enable: Boolean) {

        if (button == null) {
            return
        }
        button.isEnabled = enable
        if (button.isEnabled) {
            button.setTextColor(AppApplication.instance.getResources().getColor(R.color.dialog_confirm_enable))
        } else {
            button.setTextColor(AppApplication.instance.getResources().getColor(R.color.dialog_confirm_disable))
        }


    }


}
