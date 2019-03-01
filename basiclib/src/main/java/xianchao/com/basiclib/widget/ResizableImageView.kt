package xianchao.com.basiclib.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class ResizableImageView : ImageView {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (drawable != null) {
            var width = MeasureSpec.getSize(widthMeasureSpec)
            var height = Math.ceil((width.toFloat() * drawable.getIntrinsicHeight().toFloat() / drawable.getIntrinsicWidth().toFloat()).toDouble()).toInt()
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

}
