package belp.base.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import belp.base.R
import com.base.utils.RippleUtil

class RippleTextView : AppCompatTextView {

    companion object {
        private const val CORNER_RADIUS = 0
        private const val STROKE_WIDTH = 0
        private const val NORMAL_COLOR = Color.WHITE
        private const val RIPPLE_COLOR = Color.BLACK
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.RippleTextView, defStyleAttr, 0)

        val cornerRadius = a.getDimensionPixelSize(R.styleable.RippleTextView_rtv_corners, CORNER_RADIUS)
        val leftTopCornerRadius = a.getDimensionPixelSize(R.styleable.RippleTextView_rtv_left_top_corner, CORNER_RADIUS)
        val rightTopCornerRadius = a.getDimensionPixelSize(R.styleable.RippleTextView_rtv_right_top_corner, CORNER_RADIUS)
        val leftBottomCornerRadius = a.getDimensionPixelSize(R.styleable.RippleTextView_rtv_left_bottom_corner, CORNER_RADIUS)
        val rightBottomCornerRadius = a.getDimensionPixelSize(R.styleable.RippleTextView_rtv_right_bottom_corner, CORNER_RADIUS)
        val strokeWidth = a.getDimensionPixelSize(R.styleable.RippleTextView_rtv_stroke_width, STROKE_WIDTH)
        val strokeColor = a.getColor(R.styleable.RippleTextView_rtv_stroke_color, RIPPLE_COLOR)
        val normalColor = a.getColor(R.styleable.RippleTextView_rtv_normal_color, NORMAL_COLOR)
        val rippleColor = a.getColor(R.styleable.RippleTextView_rtv_ripple_color, strokeColor)

        a.recycle()


        background = if (strokeWidth == 0) {
            RippleUtil.getRippleDrawable(normalColor, rippleColor, cornerRadius.toFloat(),
                    leftTopCornerRadius.toFloat(), rightTopCornerRadius.toFloat(),
                    leftBottomCornerRadius.toFloat(), rightBottomCornerRadius.toFloat())
        } else {
            RippleUtil.getRippleStrokeDrawable(normalColor, rippleColor, cornerRadius.toFloat(), strokeWidth, strokeColor)
        }
    }

}