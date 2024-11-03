package com.tomassirio.io

class Keyboard {
/**
    Keypad                   Keyboard
    +-+-+-+-+                +-+-+-+-+
    |1|2|3|C|                |1|2|3|4|
    +-+-+-+-+                +-+-+-+-+
    |4|5|6|D|                |Q|W|E|R|
    +-+-+-+-+       =>       +-+-+-+-+
    |7|8|9|E|                |A|S|D|F|
    +-+-+-+-+                +-+-+-+-+
    |A|0|B|F|                |Z|X|C|V|
    +-+-+-+-+                +-+-+-+-+
**/
    private val keyboardMap: Map<Char, Char> = mapOf(
        '1' to '1', '2' to '2', '3' to '3', 'C' to '4',
        '4' to 'Q', '5' to 'W', '6' to 'E', 'D' to 'R',
        '7' to 'A', '8' to 'S', '9' to 'D', 'E' to 'F',
        'A' to 'Z', '0' to 'X', 'B' to 'C', 'F' to 'V'
    )

    private val keyboardState: MutableMap<Char, Boolean> = mutableMapOf()

    init {
        keyboardMap.values.forEach { key ->
            keyboardState[key] = false
        }
    }

    fun pressKey(physicalKey: Char) {
        val chip8Key = keyboardMap[physicalKey]
        if (chip8Key != null) {
            keyboardState[chip8Key] = true
        }
    }

    fun releaseKey(physicalKey: Char) {
        val chip8Key = keyboardMap[physicalKey]
        if (chip8Key != null) {
            keyboardState[chip8Key] = false
        }
    }

    fun isKeyPressed(chip8Key: Char): Boolean {
        return keyboardState[chip8Key] ?: false
    }

    fun clear() {
        keyboardState.keys.forEach { key ->
            keyboardState[key] = false
        }
    }
}