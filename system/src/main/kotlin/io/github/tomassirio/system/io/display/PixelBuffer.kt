package io.github.tomassirio.system.io.display

import java.util.*

class PixelBuffer(var width: Int, var height: Int) {
    private var pixels = BitSet(width * height)

    fun set(x: Int, y: Int, value: Boolean) {
        if (x in 0 until width && y in 0 until height)
            pixels.set(y * width + x, value)
    }

    fun get(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height && pixels.get(y * width + x)
    }

    fun clear() = pixels.clear()

    fun resize(newWidth: Int, newHeight: Int) {
        width = newWidth
        height = newHeight
        pixels = BitSet(width * height)
    }

    fun copyRow(from: Int, to: Int) {
        for (x in 0 until width) {
            set(x, to, get(x, from))
        }
    }

    fun clearRowRange(rows: IntRange) {
        for (y in rows) {
            for (x in 0 until width) {
                set(x, y, false)
            }
        }
    }

    fun clearPixelsInRow(y: Int, xs: IntRange) {
        for (x in xs) set(x, y, false)
    }
}
