package com.smart.liv.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.smart.liv.R


/**
 * @author JoeYe
 * @date 2023/3/8 11:27
 */
class LetterIndexView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var letters = arrayOf("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

    private val letterPaint = Paint()
    private val bgPaint = Paint()

    private var selectedIndex: Int = 0

    var letterChangedListener: LetterChangedListener? = null

    private var currentLetter: String? = null

    private var letterTextColor = Color.BLACK
    private var letterSelectedBgColor = Color.parseColor("#3C86FF")
    private var letterSelectedTextColor = Color.WHITE
    private var letterSpace = dp2px(10)
    private var letterTextSize = sp2px(12)

    private var radius: Float = 0f

    init {
        //获取属性
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.LetterIndexView)

        letterTextColor = typedArray.getColor(R.styleable.LetterIndexView_letterTextColor, letterTextColor)
        letterSelectedTextColor = typedArray.getColor(R.styleable.LetterIndexView_letterSelectedTextColor, letterSelectedTextColor)
        letterSelectedBgColor = typedArray.getColor(R.styleable.LetterIndexView_letterSelectedBgColor, letterSelectedBgColor)
        letterSpace = typedArray.getDimension(R.styleable.LetterIndexView_letterSpace, letterSpace)
        letterTextSize = typedArray.getDimension(R.styleable.LetterIndexView_letterTextSize, letterTextSize)

        typedArray.recycle()

        letterPaint.style = Paint.Style.FILL
        letterPaint.color = letterTextColor
        letterPaint.textSize = letterTextSize
        letterPaint.isAntiAlias = true

        bgPaint.style = Paint.Style.FILL
        bgPaint.color = letterSelectedBgColor
        bgPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paintText(canvas)
    }

    private fun paintText(canvas: Canvas) {
        //绘制字母，需要先计算baseline
        val letterHeight: Int = (height - paddingTop - paddingBottom) / letters.size
        val fontMetrics: FontMetricsInt = letterPaint.fontMetricsInt
        val base = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        for (i in letters.indices) {
            val dx: Float = width / 2 - letterPaint.measureText(letters[i]) / 2
            val dy: Int = paddingTop + i * letterHeight + letterHeight / 2
            val baseline = dy + base
            if (selectedIndex == i || TextUtils.equals(currentLetter, letters[i])) {
                canvas.drawCircle(
                    (width / 2).toFloat(), dy.toFloat(),
                    radius, bgPaint)

                letterPaint.color = letterSelectedTextColor
                canvas.drawText(letters[i], dx, baseline.toFloat(), letterPaint)
            } else {
                letterPaint.color = letterTextColor
                canvas.drawText(letters[i], dx, baseline.toFloat(), letterPaint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val tempLetterWidth = letterPaint.measureText("M").toInt()
        val letterWidth: Int = tempLetterWidth + paddingLeft + paddingRight
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val singleLetterHeight = letterTextSize + letterSpace

        var height = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //给定一个最小值
            height = (letters.size * singleLetterHeight + paddingTop + paddingBottom).toInt()
        }

        radius = letterWidth.coerceAtMost(singleLetterHeight.toInt()).toFloat() / 2
        setMeasuredDimension(letterWidth, height)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val y = event.y
        //计算该坐标对应的字母索引
        selectedIndex =
            ((y - paddingTop) / (height - paddingTop - paddingBottom) * letters.size).toInt()
        if (selectedIndex < 0) {
            selectedIndex = 0
        }
        if (selectedIndex >= letters.size) {
            selectedIndex = letters.size - 1
        }
        currentLetter = letters[selectedIndex]
        val letter: String = letters[selectedIndex]
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                //开始触摸
                letterChangedListener?.onLetterChanged(letter)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                //手指抬起
                letterChangedListener?.onLetterSelected(letter)
                invalidate()
            }
        }
        return true
    }

    private fun sp2px(sp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp.toFloat(),
            resources.displayMetrics
        )
    }

    private fun dp2px(dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
    }

    open fun setCurrentLetter(letter: String){
        currentLetter = letter
        selectedIndex = letters.indexOf(letter)
        invalidate()
    }

    interface LetterChangedListener{
        fun onLetterSelected(letter:String)
        fun onLetterChanged(letter:String)
    }
}