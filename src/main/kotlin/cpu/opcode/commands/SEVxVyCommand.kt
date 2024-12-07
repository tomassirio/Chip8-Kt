package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command

/**
 * 5xy0 - SE Vx, Vy
 * Skip next instruction if Vx = Vy.
 *
 * The interpreter compares register Vx to register Vy, and if they are equal, increments the program counter by 2.
 */
object SEVxVyCommand: Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerXName = (opcode.toInt() and 0x0F00) shr 8
        val registerYName = (opcode.toInt() and 0x00F0) shr 4
        val registerX = cpu.registers[registerXName.toString()]
        val registerY = cpu.registers[registerYName.toString()]

        if (registerX.read() == registerY.read()) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }
}