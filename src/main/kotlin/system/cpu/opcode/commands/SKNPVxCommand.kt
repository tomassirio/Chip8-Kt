package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

/**
 *  ExA1 - SKNP Vx
 *  Skip next instruction if key with the value of Vx is not pressed.
 *
 * Checks the keyboard, and if the key corresponding to the value of Vx is currently in the up position, PC is increased by 2.
 */

object SKNPVxCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val vx = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val vxValue = vx.read()

        val isPressed = cpu.keyboardState.isKeyPressed(vxValue.toInt().toChar())

        if (!isPressed) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }
}