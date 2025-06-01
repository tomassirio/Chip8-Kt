package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.Command
import com.tomassirio.system.memory.util.toUByteAt

/**
 * Fx65 - LD Vx, [I]
 * Read registers V0 through Vx from memory starting at location I.
 *
 * The interpreter reads values from memory starting at location I into registers V0 through Vx.
 *
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun ldVxICommand(): Command {
    return Command {cpu, opcode ->
        val registerIndex = ((opcode and 0x0F00u).toInt() shr 8)

        val memoryAddress = cpu.I.read().toInt()

        // Load memory values into registers V0 through Vx
        for (i in 0..registerIndex) {
            val memoryValue = cpu.memory.read(memoryAddress + i) { bytes, addr ->
                bytes.toUByteAt(addr)
            }
            cpu.registers[i].write(memoryValue)
        }
    }
}