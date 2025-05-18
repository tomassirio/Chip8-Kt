package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 *  Ex9E - SKP Vx
 *  Skip next instruction if key with the value of Vx is pressed.
 *
 * Checks the keyboard, and if the key corresponding to the value of Vx is currently in the down position, PC is increased by 2.
 */
object SKPVxCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val vx = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val vxValue = vx.read()

        val isPressed = cpu.keyboard.isKeyPressed(vxValue.toInt().toChar())

        if (isPressed) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }
}