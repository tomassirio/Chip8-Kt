package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command

/**
 * Fx55 - LD [I], Vx
 * Store registers V0 through Vx in memory starting at location I.
 *
 * The interpreter copies the values of registers V0 through Vx into memory, starting at the address in I.
 */

fun ldIVxCommand(): Command {
    return Command {cpu, opcode ->
        val registerIndex = ((opcode and 0x0F00u).toInt() shr 8)

        val memoryAddress = cpu.I.read().toInt()

        // Store registers V0 through Vx in memory
        for (i in 0..registerIndex) {
            val registerValue = cpu.registers[i].read()
            cpu.memory.writeByte(memoryAddress + i, registerValue)
        }
        cpu.I.write((cpu.I.read() + (registerIndex.toUShort() + 1u)).toUShort())
    }
}