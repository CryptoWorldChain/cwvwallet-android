package fanrong.cwvwalled.ui.view;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by lenovo on 2019/1/30
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9f;
    private static final float MIN_ALPHA = 0.5f;

    private static float defaultScale = 0.9f;

    @Override
    public void transformPage(@NonNull View view, float v) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (v < -1){
            view.setAlpha(0);
            view.setScaleX(defaultScale);
            view.setScaleY(defaultScale);
        } else if (v <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(v));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (v < 0){
                view.setTranslationX(horzMargin - vertMargin / 2);
            }else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        } else {
            view.setAlpha(0);
            view.setScaleX(defaultScale);
            view.setScaleY(defaultScale);
        }
    }
}
