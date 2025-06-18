package com.tomassirio.emulation.chip8.controller.handler

import com.tomassirio.emulation.chip8.system.io.keyboard.KeyboardState
import com.tomassirio.emulation.chip8.system.register.utils.RegisterSet

class KeyHandler(
    private val keyboardState: KeyboardState,
    private val registers: RegisterSet,
) {
    fun onKeyPressed(chip8Key: Char) {
        keyboardState.pressKey(chip8Key)
    }

    fun onKeyReleased(chip8Key: Char) {
        keyboardState.releaseKey(chip8Key)

        if (keyboardState.isWaitingForKey &&
            keyboardState.registerToStoreKeyIn != null &&
            keyboardState.waitingKeyPressed == chip8Key) {

            val registerIndex = keyboardState.registerToStoreKeyIn!!
            registers[registerIndex].write(
                KeyboardState.KEYS.indexOf(chip8Key).toUByte()
            )
            keyboardState.clearWaitingForKey()
        }
    }
}