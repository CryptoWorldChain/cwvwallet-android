package net.sourceforge.listener

import android.app.Dialog

abstract class FRPayWayClickListener {

    open fun wechatClick(dialog:Dialog){
    }
    open fun alipayClick(dialog:Dialog){
    }
    open fun payPalClick(dialog:Dialog){
    }
}