package com.herald.currencyapp.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.herald.currencyapp.R

class BarChartView(context: Context) : View(context) {

    private val paint = Paint()
    private val textPaint = Paint()
    private val bars = mutableListOf<Bar>()
    private var horizontalMargins = 150f
    private var topMargins = 120f
    private var marginTopText = 10f
    private var marginBotText = 50f
    private var maxValue = 0f

    init {
        paint.color = ContextCompat.getColor(context, R.color.primary)
        paint.strokeWidth = 2f
        textPaint.color = ContextCompat.getColor(context, R.color.black)
        textPaint.textSize = 42f
        textPaint.textAlign = Paint.Align.CENTER
    }

    fun addBar(bar: Bar) {
        bars.add(bar)
        maxValue = maxOf(maxValue, bar.value.toFloat() )
        invalidate()
    }

    fun addBars(bars: List<Bar>) {
        maxValue = bars.maxOf { it.value.toFloat() }
        this.bars.addAll(0, bars)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val barHeightScale = (height - topMargins - marginBotText) / maxValue
        Log.i("TAG", "onDraw: $barHeightScale")
        val barWidth = (width - horizontalMargins) / bars.size
        var barLeft = horizontalMargins
        for (bar in bars) {
            val barHeight = (bar.value * barHeightScale).toFloat()
            canvas.run {
                drawRect(
                    /* left = */ barLeft,
                    /* top = */ (height - marginBotText - barHeight),
                    /* right = */ barLeft + barWidth - horizontalMargins,
                    /* bottom = */ height.toFloat() - marginBotText,
                    /* paint = */ paint
                )
                drawText(
                    bar.date,
                    barLeft + barWidth / 4,
                    height.toFloat(),
                    textPaint
                )
                drawText(
                    /* text = */ String.format("%.3f",bar.value),
                    /* x = */ barLeft + barWidth / 4,
                    /* y = */ (height - barHeight - marginTopText - marginBotText),
                    /* paint = */ textPaint
                )
            }
            barLeft += barWidth
        }
    }

    class Bar(val value: Double, val date: String)
}

