package net.sourceforge.listener

import android.app.Dialog

interface FRPasswordDialogConfirmListener {
    fun onConfirmClick(dialog: Dialog, password: String)
}