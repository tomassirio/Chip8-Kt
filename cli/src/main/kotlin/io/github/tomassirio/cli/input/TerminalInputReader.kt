package io.github.tomassirio.cli.input

import org.jline.utils.NonBlockingReader

class TerminalInputReader(private val reader: NonBlockingReader) {
    fun pollChars(): List<Char> {
        val chars = mutableListOf<Char>()
        while (true) {
            val c = reader.read(1L)
            if (c == NonBlockingReader.READ_EXPIRED || c == NonBlockingReader.EOF) break
            chars.add(c.toChar())
        }
        return chars
    }
}
