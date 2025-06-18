package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * Fx0A - LD Vx, K
 * Wait for a key press, store the value of the key in Vx.
 *
 * All execution stops until a key is pressed, then the value of that key is stored in Vx.
 */
fun ldVxKCommand(): Command {
    return Command { cpu, opcode ->
        val vX = (opcode and 0xF00u).toInt() shr 8

        cpu.keyboardState.setWaitingForKey(vX)
    }
}