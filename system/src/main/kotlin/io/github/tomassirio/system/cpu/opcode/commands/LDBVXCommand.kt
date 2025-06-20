package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 * Fx33 - LD B, Vx
 * Store BCD representation of Vx in memory locations I, I+1, and I+2.
 *
 * The interpreter takes the decimal value of Vx,
 *  and places the hundreds digit in memory at location in I,
 *  the tens digit at location I+1, and the ones digit at location I+2.
 */

fun ldBVxCommand(): Command {
    return Command { cpu, opcode ->
        val vX = ((opcode and 0x0F00u).toInt() shr 8)
        val value = cpu.registers[vX].read().toInt()

        // Calculate BCD digits
        val hundreds = value / 100
        val tens = (value % 100) / 10
        val ones = value % 10

        val memoryAddress = cpu.I.read().toInt()

        cpu.memory.writeByte(memoryAddress, hundreds.toUByte())
        cpu.memory.writeByte(memoryAddress + 1, tens.toUByte())
        cpu.memory.writeByte(memoryAddress + 2, ones.toUByte())
    }
}