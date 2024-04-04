package com.example.draganddraw.database

class Color() {
    private var color0 = 0

    fun setColor(colorInt: Int): Int {
        color0 = colorInt
        return color0
    }

    fun getColor(): Int {
        return color0
    }

}