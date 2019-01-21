package belp.base.widgets

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import belp.base.R
import belp.base.utils.RippleUtil

class RippleConstraintLayout : ConstraintLayout {

    companion object {
        private const val CORNER_RADIUS = 0
        private const val STROKE_WIDTH = 0
        private const val RIPPLE_COLOR = Color.DKGRAY
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        if (Build.VERSION.SDK_INT >= 23) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.RippleConstraintLayout, defStyleAttr, 0)

            val cornerRadius = a.getDimensionPixelSize(R.styleable.RippleConstraintLayout_rcl_corners, CORNER_RADIUS)
            val leftTopCornerRadius =
                a.getDimensionPixelSize(R.styleable.RippleConstraintLayout_rcl_left_top_corner, CORNER_RADIUS)
            val rightTopCornerRadius =
                a.getDimensionPixelSize(R.styleable.RippleConstraintLayout_rcl_right_top_corner, CORNER_RADIUS)
            val leftBottomCornerRadius =
                a.getDimensionPixelSize(R.styleable.RippleConstraintLayout_rcl_left_bottom_corner, CORNER_RADIUS)
            val rightBottomCornerRadius =
                a.getDimensionPixelSize(R.styleable.RippleConstraintLayout_rcl_right_bottom_corner, CORNER_RADIUS)
            val strokeWidth = a.getDimensionPixelSize(R.styleable.RippleConstraintLayout_rcl_stroke_width, STROKE_WIDTH)
            val strokeColor = a.getColor(R.styleable.RippleConstraintLayout_rcl_stroke_color, RIPPLE_COLOR)
            val rippleColor = a.getColor(R.styleable.RippleConstraintLayout_rcl_ripple_color, RIPPLE_COLOR)

            a.recycle()

            foreground = if (strokeWidth == 0) {
                RippleUtil.getRippleDrawable(
                    Color.TRANSPARENT, rippleColor, cornerRadius.toFloat(),
                    leftTopCornerRadius.toFloat(), rightTopCornerRadius.toFloat(),
                    leftBottomCornerRadius.toFloat(), rightBottomCornerRadius.toFloat()
                )
            } else {
                RippleUtil.getRippleStrokeDrawable(
                    Color.TRANSPARENT,
                    rippleColor,
                    cornerRadius.toFloat(),
                    strokeWidth,
                    strokeColor
                )
            }

        }
    }

}