package fanrong.cwvwalled.utils;

import android.util.DisplayMetrics;

import java.util.HashMap;
import java.util.Map;

import fanrong.cwvwalled.base.AppApplication;
import fanrong.cwvwalled.R;
import fanrong.cwvwalled.ui.activity.MainActivity;

/**
 * 背景图， 处理类
 */
public class BgUtils {

    public static final String YINDAO = "引导页";


    static Map<String, Integer> bigScreen = new HashMap<String, Integer>() {
        {
            put("引导页", R.drawable.splash_bg_chang);
//            put("理财", R.drawable.financial_bg_chang);
//            put("收款", R.drawable.recept_bg_chang);
//            put("登录", R.drawable.recept_bg_chang);


        }
    };
    static Map<String, Integer> commonScreen = new HashMap<String, Integer>() {
        {
            put("引导页", R.drawable.splash_bg);
//            put("理财", R.drawable.financial_bg);
//            put("收款", R.drawable.recept_bg);
        }
    };

    public static int getBg(String String) {
        DisplayMetrics displayMetrics = AppApplication.instance.getResources().getDisplayMetrics();
        double v = 2160.0 / 1080.0;
        if (Math.abs(Double.valueOf(displayMetrics.heightPixels) / displayMetrics.widthPixels - v) < 0.2) {
            return bigScreen.get(String);
        } else if (Double.valueOf(displayMetrics.heightPixels) / displayMetrics.widthPixels - v > 0) {
            return bigScreen.get(String);
        } else {
            return commonScreen.get(String);
        }
    }

}
