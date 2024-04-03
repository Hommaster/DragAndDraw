package com.example.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.draganddraw.constance.Constance
import com.example.draganddraw.database.Box
import kotlin.math.atan2

class BoxDrawingView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var currentBox: Box? = null
    private var boxes = mutableListOf<Box>()
    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    private var lastRotation = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {

        var current = PointF(event.x, event.y)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // Reset drawing state
                currentBox = Box(current).also {
                    boxes.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                updateCurrentBox(current)
                //when the second finger appears, we mathematically calculate the degrees between the two fingers
                if (event.pointerCount == 2) {
                    val firstPointer = PointF(event.getX(0), event.getY(0))
                    val secondPointer = PointF(event.getX(1), event.getY(1))
                    val deltaX = firstPointer.x - secondPointer.x
                    val deltaY = firstPointer.y - secondPointer.y
                    val degreeInRad = atan2(deltaX, deltaY).toDouble()
                    //save angle to database
                    currentBox?.setAngle(Math.toDegrees(degreeInRad).toFloat())
                }
            }
            MotionEvent.ACTION_UP -> {
                updateCurrentBox(current)
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                currentBox = null
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        // Fill the background
        canvas.drawPaint(backgroundPaint)

        val angle = currentBox?.getAngle()

        boxes.forEach { box ->
            //get angle from database
            val angle = box.getAngle()
            val px = (box.start.x + box.end.x)/2
            val py = (box.start.y + box.end.y)/2
            canvas.save()
            //without this "if" thrown null when drawing
            if (angle != null) {
                canvas.rotate(angle, px, py)
            }
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
            canvas.restore()
        }
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }

    override fun onSaveInstanceState(): Bundle {
        Log.i("restore1", "state1")
        val state = super.onSaveInstanceState()
        val bundle: Bundle = Bundle()
        bundle.putParcelableArrayList(Constance.BOXES, ArrayList<Parcelable>(boxes))
        bundle.putParcelable(Constance.VIEW_STATE, state)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        Log.i("restore2", "state2")
        if (state is Bundle) {
            Log.i("restore3", "state3")
            if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                boxes = state.getParcelableArrayList(Constance.BOXES, Box::class.java)?.toMutableList() ?: mutableListOf()
                super.onRestoreInstanceState(state.getParcelable(Constance.VIEW_STATE, Box::class.java))
            } else {
                boxes = state.getParcelableArrayList<Box>(Constance.BOXES)?.toMutableList() ?: mutableListOf()
                super.onRestoreInstanceState(state.getParcelable(Constance.VIEW_STATE))
            }
        }
    }
}