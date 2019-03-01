package fanrong.cwvwalled.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;


import java.lang.reflect.Field;

import fanrong.cwvwalled.R;
import fanrong.cwvwalled.base.AppApplication;

public class MsgPopupWindow {


    private static PopupWindow popupWindow;

    public synchronized static PopupWindow getPopupwindow() {

        try {
            if (popupWindow == null) {
                View inflate = LayoutInflater.from(AppApplication.instance).inflate(R.layout.layout_popup_msg, null);
                popupWindow = new PopupWindow(inflate);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setAnimationStyle(R.style.pop_animation);
                popupWindow.setClippingEnabled(false);
                Field mClipToScreen = null;
                mClipToScreen = PopupWindow.class.getDeclaredField("mClipToScreen");
                mClipToScreen.setAccessible(true);
                mClipToScreen.set(popupWindow, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return popupWindow;
    }

}
