package com.tomassirio.controller

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.io.KeyboardState

class EmulatorController(
    val cpu: CPU
) {
    fun loadRom(data: ByteArray) {
        cpu.loadRom(data)
    }

    fun tick() {
        cpu.runCycle()
        // Update timers
        cpu.DT.tick()
        cpu.ST.tick()
    }

    fun onKeyPressed(chip8Key: Char) {
        cpu.keyboardState.pressKey(chip8Key)

        // If CPU is waiting for a key, store it
        if (cpu.waitingForKey && cpu.registerToStoreKeyIn != null) {
            val registerIndex = cpu.registerToStoreKeyIn!!
            cpu.registers[registerIndex].write(
                KeyboardState.KEYS.indexOf(chip8Key).toUByte()
            )
            cpu.waitingForKey = false
            cpu.registerToStoreKeyIn = null
        }
    }

    fun onKeyReleased(chip8Key: Char) {
        cpu.keyboardState.releaseKey(chip8Key)
    }
}