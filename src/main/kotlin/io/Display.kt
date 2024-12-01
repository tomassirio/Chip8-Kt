package com.tomassirio.io

import java.util.BitSet

class Display(val width: Int = 64, val height: Int = 32) {

    private val gfx: BitSet = BitSet(width * height)

    fun setPixel(x: Int, y: Int, value: Boolean) {
        val index = y * width + x
        gfx.set(index, value)
    }

    fun getPixel(x: Int, y: Int): Boolean {
        val index = y * width + x
        return gfx.get(index)
    }

    fun clearScreen() {
        gfx.clear()
    }
}
