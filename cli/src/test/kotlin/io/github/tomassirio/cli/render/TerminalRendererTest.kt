package io.github.tomassirio.cli.render

import io.github.tomassirio.system.io.display.DisplayState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import java.io.StringWriter

private const val ESC = ''

class TerminalRendererTest {

    private val out = StringWriter()
    private val renderer = TerminalRenderer(PrintWriter(out))

    @Test
    fun `skips writing when frame is unchanged`() {
        val display = DisplayState()
        display.setPixel(0, 0, true)

        renderer.render(display)
        val firstWrite = out.toString()
        renderer.render(display)

        assertThat(out.toString()).isEqualTo(firstWrite)
    }

    @Test
    fun `only rewrites rows that changed`() {
        val display = DisplayState()
        display.setPixel(0, 0, true)
        renderer.render(display)
        out.buffer.setLength(0)

        display.setPixel(0, 5, true)
        renderer.render(display)

        val output = out.toString()
        assertThat(output).contains("$ESC[6;1H")
        assertThat(output).doesNotContain("$ESC[1;1H")
    }

    @Test
    fun `wraps changed output in synchronized-update escapes`() {
        val display = DisplayState()
        display.setPixel(0, 0, true)

        renderer.render(display)

        val output = out.toString()
        assertThat(output).startsWith("$ESC[?2026h")
        assertThat(output).endsWith("$ESC[?2026l")
    }

    @Test
    fun `pixel that goes off for a single frame stays visually on to avoid XOR-draw flicker`() {
        val display = DisplayState()
        display.setPixel(3, 3, true)
        renderer.render(display)

        display.setPixel(3, 3, false)
        out.buffer.setLength(0)
        renderer.render(display)

        assertThat(out.toString()).isEmpty()
    }

    @Test
    fun `pixel off for two consecutive frames actually renders off`() {
        val display = DisplayState()
        display.setPixel(3, 3, true)
        renderer.render(display)

        display.setPixel(3, 3, false)
        renderer.render(display)
        out.buffer.setLength(0)
        renderer.render(display)

        assertThat(out.toString()).contains("$ESC[4;1H")
    }
}
