package com.example.draganddraw.database

import android.graphics.PointF
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Box(val start: PointF) : Parcelable {
    var end: PointF = start
    private var angle: Float? = null
    private var colorNumber: Int? = null

    val left: Float
        get() = Math.min(start.x, end.x)

    val right: Float
        get() = Math.max(start.x, end.x)

    val top: Float
        get() = Math.min(start.y, end.y)

    val bottom: Float
        get() = Math.max(start.y, end.y)

    fun setAngle(angle: Float) {
        this.angle = angle
    }

    fun getAngle(): Float? {
        return angle
    }
}
