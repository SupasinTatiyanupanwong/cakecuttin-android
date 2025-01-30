package dev.supasintatiyanupanwong.apps.android.cakecuttin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.properties.Delegates

class CuttinOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val rect = RectF()
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, context.resources.displayMetrics)
    }

    @delegate:ColorInt
    var color: Int by Delegates.observable(Color.RED) { _, _, _ -> invalidate() }

    var shape: Int by Delegates.observable(0) { _, _, _ -> invalidate() }

    var count: Int by Delegates.observable(3) { _, _, _ -> invalidate() }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = color

        val centerX = width * 0.5f
        val centerY = height * 0.5f
        val radius = min(centerX, centerY)

        val halfStroke = paint.strokeWidth * 0.5f

        rect.set(
            centerX - radius + halfStroke,
            centerY - radius + halfStroke,
            centerX + radius - halfStroke,
            centerY + radius - halfStroke
        )

        when (shape) {
            SHAPE_TYPE_CIRCLE -> {
                canvas.drawCircle(centerX, centerY, radius - halfStroke, paint)

                val deg = 360f / count
                (0 until count).forEach {
                    val rad = (-90 + it * deg) * PI.toFloat() / 180
                    canvas.drawLine(
                        centerX,
                        centerY,
                        centerX + cos(rad) * radius,
                        centerY + sin(rad) * radius,
                        paint
                    )
                }
            }

            SHAPE_TYPE_SECT_X -> {
                canvas.drawRect(rect, paint)

                val sliceWidth = rect.width() / count
                (0..count).forEach {
                    val x = rect.left + it * sliceWidth
                    canvas.drawLine(x, rect.top, x, rect.bottom, paint)
                }
            }

            SHAPE_TYPE_SECT_Y -> {
                canvas.drawRect(rect, paint)

                val sliceHeight = rect.height() / count
                (0..count).forEach {
                    val y = rect.top + it * sliceHeight
                    canvas.drawLine(rect.left, y, rect.right, y, paint)
                }
            }

            SHAPE_TYPE_RECT -> {
                canvas.drawRect(rect, paint)

                var rows = 1
                var cols = count
                (1..sqrt(count.toFloat()).toInt()).forEach {
                    if (count % it == 0) {
                        rows = it
                        cols = count / it
                    }
                }

                val sliceWidth = rect.width() / cols
                (1 until cols).forEach {
                    val x = rect.left + it * sliceWidth
                    canvas.drawLine(x, rect.top, x, rect.bottom, paint)
                }

                val sliceHeight = rect.height() / rows
                (1 until rows).forEach {
                    val y = rect.top + it * sliceHeight
                    canvas.drawLine(rect.left, y, rect.right, y, paint)
                }
            }
        }
    }

    companion object {
        const val SHAPE_TYPE_CIRCLE = 0
        const val SHAPE_TYPE_SECT_X = 1
        const val SHAPE_TYPE_SECT_Y = 2
        const val SHAPE_TYPE_RECT = 3
    }
}
