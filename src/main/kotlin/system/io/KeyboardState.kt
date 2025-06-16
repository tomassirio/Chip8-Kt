package com.tomassirio.system.io

import com.tomassirio.constants.KEYBOARD_KEYS

class KeyboardState {
    val keyboardState = mutableMapOf<Char, Boolean>().apply {
        KEYS.forEach { this[it] = false }
    }

    var isWaitingForKey: Boolean = false
        private set

    var registerToStoreKeyIn: Int? = null
        private set

    var waitingKeyPressed: Char? = null
        private set

    fun pressKey(physicalKey: Char) {
        if (keyboardState.containsKey(physicalKey)) {
            keyboardState[physicalKey] = true

            if (isWaitingForKey && waitingKeyPressed == null) {
                waitingKeyPressed = physicalKey
            }
        }
    }

    fun releaseKey(physicalKey: Char) {
        if (keyboardState.containsKey(physicalKey)) {
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
        clearWaitingForKey()
    }

    fun clearWaitingForKey() {
        isWaitingForKey = false
        registerToStoreKeyIn = null
    }

    fun setWaitingForKey(register: Int) {
        isWaitingForKey = true
        registerToStoreKeyIn = register
    }

    companion object {
        val KEYS = KEYBOARD_KEYS.toList()
    }
}
