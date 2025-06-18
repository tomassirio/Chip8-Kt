package com.tomassirio.emulation.chip8.system.io.display

class DisplayScroller(
    private val buffer: PixelBuffer,
    private val width: Int,
    private val height: Int
) {
    fun scrollDown(lines: Int) {
        for (y in height - 1 downTo lines) buffer.copyRow(y - lines, y)
        buffer.clearRowRange(0 until lines)
    }

    fun scrollRight(pixels: Int) {
        for (y in 0 until height) {
            for (x in width - 1 downTo pixels)
                buffer.set(x, y, buffer.get(x - pixels, y))
            buffer.clearPixelsInRow(y, 0 until pixels)
        }
    }

    fun scrollLeft(pixels: Int) {
        for (y in 0 until height) {
            for (x in 0 until width - pixels)
                buffer.set(x, y, buffer.get(x + pixels, y))
            buffer.clearPixelsInRow(y, (width - pixels) until width)
        }
    }

}