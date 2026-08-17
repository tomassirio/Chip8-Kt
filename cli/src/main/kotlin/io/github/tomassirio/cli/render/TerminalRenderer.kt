package io.github.tomassirio.cli.render

import io.github.tomassirio.system.io.display.DisplayState
import io.github.tomassirio.system.io.display.DisplayType
import java.io.PrintWriter

private const val ON = "██"
private const val OFF = "  "
private const val CLEAR_AND_HIDE_CURSOR = "[2J[?25l"
private const val SYNC_UPDATE_START = "[?2026h"
private const val SYNC_UPDATE_END = "[?2026l"

class TerminalRenderer(private val writer: PrintWriter) {
    private var previousRaw: Array<BooleanArray>? = null
    private var lastLines: Array<String>? = null

    fun clearAndHideCursor() {
        writer.print(CLEAR_AND_HIDE_CURSOR)
        writer.flush()
    }

    fun render(display: DisplayState) {
        val (width, height) = if (display.isExtended()) {
            DisplayType.SCHIP8.width to DisplayType.SCHIP8.height
        } else {
            DisplayType.CHIP8.width to DisplayType.CHIP8.height
        }

        val prevRaw = previousRaw.takeIf { it != null && it.size == height && it[0].size == width }
        val rawFrame = Array(height) { y -> BooleanArray(width) { x -> display.getPixel(x, y) } }

        // CHIP-8 sprites are drawn via XOR erase+redraw, which can toggle a pixel on/off
        // every single emulated frame. Rendering that raw toggle looks like flicker, so a
        // pixel stays visually on for one extra frame after it last went on (classic
        // anti-flicker technique used by other CHIP-8 interpreters).
        val lines = Array(height) { y ->
            val line = StringBuilder(width * 2)
            for (x in 0 until width) {
                val visible = rawFrame[y][x] || (prevRaw?.get(y)?.get(x) == true)
                line.append(if (visible) ON else OFF)
            }
            line.toString()
        }
        previousRaw = rawFrame

        val previous = lastLines
        if (previous.contentEquals(lines)) return
        lastLines = lines

        val output = StringBuilder(SYNC_UPDATE_START)
        for (y in lines.indices) {
            if (previous != null && previous.size == lines.size && previous[y] == lines[y]) continue
            output.append("[").append(y + 1).append(";1H").append(lines[y])
        }
        output.append(SYNC_UPDATE_END)

        writer.print(output)
        writer.flush()
    }
}
