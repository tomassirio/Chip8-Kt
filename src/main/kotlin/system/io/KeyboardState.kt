package com.tomassirio.system.io

class KeyboardState {
    private val keyboardState = mutableMapOf<Char, Boolean>().apply {
        KEYS.forEach { this[it] = false }
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

    companion object {
        val KEYS = "0123456789ABCDEF".toList()
    }
}