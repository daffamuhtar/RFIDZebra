package com.example.rfidzebra.ui.feature.home.locate.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.rfidzebra.R
import com.example.rfidzebra.ui.feature.home.locate.utils.Constants

class RangeGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val TAG = "RangeGraph"
        private const val PADDING_5 = 5
        private const val FONT_COLOR = Color.BLACK
        private val LINE_COLOR = Color.parseColor("#008080")
    }

    // Graphics
    private val colorNeutral = Color.parseColor("#BFCBBF")
    private val colorInRange = Color.parseColor("#ff7043")

    // State
    private var mMin = 0
    private var mMax = 100
    private var mValue = 50
    private var mFontSize = resources.getDimensionPixelSize(R.dimen.locationing_font_size)
    private lateinit var mPaint: Paint

    // Layout
    private var mWidth = 200
    private var mHeight = 500

    init {
        attrs?.let {
            val a: TypedArray = context.theme.obtainStyledAttributes(it, R.styleable.RangeGraph, 0, 0)
            try {
                mMin = a.getInteger(R.styleable.RangeGraph_minimum, 0)
                mMax = a.getInteger(R.styleable.RangeGraph_maximum, 100)
            } finally {
                a.recycle()
            }
        }
        commonSetup()
    }

    private fun commonSetup() {
        mPaint = Paint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
        mPaint.color = colorNeutral
        // Scale the desired text size to match screen density
        mPaint.textSize = mFontSize * resources.displayMetrics.density
        setPadding(PADDING_5, PADDING_5, PADDING_5, PADDING_5)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        mWidth = parentWidth / 2
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        mHeight = parentHeight
        setMeasuredDimension(parentWidth, parentHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the bar outline
        val top = paddingTop
        val bot = mHeight - paddingBottom
        val leftSide = (mWidth * 0.75f).toInt()
        val rightSide = (mWidth * 1.25f).toInt()

        // Preserve the old style
        val oldStyle = mPaint.style
        mPaint.style = Paint.Style.STROKE
        mPaint.color = colorNeutral
        canvas.drawRect(leftSide.toFloat(), top.toFloat(), rightSide.toFloat(), bot.toFloat(), mPaint)

        // Reset to old style
        mPaint.style = oldStyle

        // Now draw the bar graph.
        mPaint.color = colorInRange
        // Value expressed as percentage of height
        val barHeight = ((mValue.toFloat() / mMax) * mHeight).toInt()
        Constants.logAsMessage(Constants.TYPE_DEBUG, TAG, "drawRect($leftSide ${mHeight - barHeight} $rightSide $bot)")
        canvas.drawRect(leftSide.toFloat(), (bot - barHeight).toFloat(), rightSide.toFloat(), bot.toFloat(), mPaint)

        // Reset to old style
        mPaint.style = oldStyle

        // First put in two tick marks before changing the color
        mPaint.color = LINE_COLOR

        // Draw the percentage text
        mPaint.textSize = 25f
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.isFakeBoldText = true
        mPaint.color = FONT_COLOR
        canvas.drawText("$mValue%", (leftSide + (rightSide - leftSide) / 2).toFloat(), (top + (bot - top) / 2).toFloat(), mPaint)
    }

    fun getValue(): Int {
        return mValue
    }

    fun setValue(value: Short) {
        mValue = value.toInt()
        invalidate()
    }
}
