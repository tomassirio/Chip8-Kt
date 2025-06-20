package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 * Fx65 - LD Vx, [I]
 * Read registers V0 through Vx from memory starting at location I.
 *
 * The interpreter reads values from memory starting at location I into registers V0 through Vx.
 *
 */
fun ldVxICommand(): Command {
    return Command { cpu, opcode ->
        val registerIndex = ((opcode and 0x0F00u).toInt() shr 8)

        val memoryAddress = cpu.I.read().toInt()

        // Load memory values into registers V0 through Vx
        for (i in 0..registerIndex) {
            val memoryValue = cpu.memory.readByte(memoryAddress + i)
            cpu.registers[i].write(memoryValue)
        }
        cpu.I.write((cpu.I.read() + (registerIndex.toUShort() + 1u)).toUShort())
    }
}