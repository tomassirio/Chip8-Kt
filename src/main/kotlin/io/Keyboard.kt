package com.tomassirio.io

class Keyboard {
    private val keyboardState = mutableMapOf<Char, Boolean>().apply {
        "0123456789ABCDEF".forEach { this[it] = false }
    }

    fun pressKey(physicalKey: Char) {
        val chip8Key = keyboardState[physicalKey]
        if (chip8Key != null) {
            keyboardState[physicalKey] = true
        }
    }

    fun releaseKey(physicalKey: Char) {
        val chip8Key = keyboardState[physicalKey]
        if (chip8Key != null) {
            keyboardState[physicalKey] = false
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