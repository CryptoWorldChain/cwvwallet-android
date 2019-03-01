package fanrong.cwvwalled.listener

import android.app.Dialog
import fanrong.cwvwalled.ui.view.InputPasswordDialog

interface FRDialogBtnListener {
    fun onCancel(dialog: Dialog)
    fun onConfirm(dialog: Dialog)
}