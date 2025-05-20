package com.tomassirio.controller

import com.tomassirio.system.cpu.CPU

class EmulatorController(
    val cpu: CPU,
) {
    private fun tick() {
        cpu.runCycle()
    }

//    private fun onKeyPressed(chip8Key: Char) {
//        keyboardState.pressKey(chip8Key)
//
//        if (cpu.waitingForKey && cpu.registerToStoreKeyIn != null) {
//            val registerIndex = cpu.registerToStoreKeyIn!!
//            cpu.registers[registerIndex].write(
//                KeyboardState.KEYS.indexOf(chip8Key).toUByte()
//            )
//            cpu.waitingForKey = false
//            cpu.registerToStoreKeyIn = null
//        }
//    }

//    fun reset() {
//        cpu.reset()
//    }

    fun debug() {
        StringBuilder().apply {
            append("CPU State:\n")
            append("Short Registers\n")
            append("$cpu.pc\n")
            append("$cpu.sp\n")
            append("$cpu.I\n\n")
            append("Timer Registers\n")
            append("$cpu.DT\n")
            append("$cpu.ST\n\n")
            append("Keyboard: ${cpu.keyboardState}\n")
            append("Display: ${cpu.displayState}\n\n")
            append("Byte Registers:\n")
            cpu.registers.forEach {
                append("$it\n")
            }
            append("\n")
            append("Memory: $cpu.memory\n")
            println(toString())
        }
    }
}