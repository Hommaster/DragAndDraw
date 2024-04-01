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

private const val TAG = "BoxDrawingView"

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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val current = PointF(event!!.x, event.y)
        var action = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                // Reset drawing state
                currentBox = Box(current).also {
                    boxes.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentBox(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentBox(current)
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }

        Log.i(TAG, "$action at x=${current.x}, y=${current.y}")

        return true
    }

    override fun onDraw(canvas: Canvas) {
        // Fill the background
        canvas.drawPaint(backgroundPaint)

        boxes.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
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
        Log.i("restore4", "")
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