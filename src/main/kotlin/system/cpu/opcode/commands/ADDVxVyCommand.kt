package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

/**
 * 8xy4 - ADD Vx, Vy
 * Set Vx = Vx + Vy, set VF = carry.
 *
 * The values of Vx and Vy are added together. If the result is greater than 8 bits (i.e., > 255), VF is set to 1, otherwise 0. Only the lowest 8 bits of the result are kept, and stored in Vx.
 */
object ADDVxVyCommand: Command{
    override fun execute(cpu: CPU, opcode: UShort) {
        val vX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val vY = cpu.registers[(opcode and 0xF0u).toInt() shr 4]

        val sum = vX.read().plus(vY.read())
        vX.write(sum.toUByte())
        cpu.registers[0xF].write(if (sum > 0xFFu) 1u else 0u)
    }
}