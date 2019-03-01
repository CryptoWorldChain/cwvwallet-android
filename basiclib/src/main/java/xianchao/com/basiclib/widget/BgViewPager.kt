package xianchao.com.basiclib.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout

class BgViewPager : ViewPager {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val drawable = background
        if (drawable != null) {
            val width = View.MeasureSpec.getSize(widthMeasureSpec)
            val height = Math.ceil((width.toFloat() * drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth.toFloat()).toDouble()).toInt()

            super.onMeasure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
                    , View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
