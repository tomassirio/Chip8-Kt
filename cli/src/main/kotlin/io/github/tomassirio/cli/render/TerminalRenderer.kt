package io.github.tomassirio.cli.render

import io.github.tomassirio.system.io.display.DisplayState
import io.github.tomassirio.system.io.display.DisplayType
import java.io.PrintWriter

private const val ON = "██"
private const val OFF = "  "
private const val CURSOR_HOME = "[H"

class TerminalRenderer(private val writer: PrintWriter) {
    fun render(display: DisplayState) {
        val (width, height) = if (display.isExtended()) {
            DisplayType.SCHIP8.width to DisplayType.SCHIP8.height
        } else {
            DisplayType.CHIP8.width to DisplayType.CHIP8.height
        }

        val frame = StringBuilder(CURSOR_HOME)
        for (y in 0 until height) {
            for (x in 0 until width) {
                frame.append(if (display.getPixel(x, y)) ON else OFF)
            }
            frame.append('\n')
        }

        writer.print(frame)
        writer.flush()
    }
}
