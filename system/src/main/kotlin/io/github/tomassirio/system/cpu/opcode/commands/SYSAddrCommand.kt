package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 * 0nnn - SYS addr
 * Jump to a machine code routine at nnn.
 *
 * This instruction is only used on the old computers on which Chip-8 was originally implemented. It is ignored by modern interpreters.
 */
fun sysAddrCommand(): Command {
    return Command { cpu, opcode ->
        // No longer implemented
    }
}