package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.opcode.Command

/**
 * 8xy5 - SUB Vx, Vy
 * Set Vx = Vx - Vy, set VF = NOT borrow.
 *
 * If Vx > Vy, then VF is set to 1, otherwise 0. Then Vx is subtracted by Vy, and the results stored in Vx.
 */
fun subVxVyCommand(): Command {
    return Command { cpu, opcode ->
        val x = ((opcode and 0x0F00u).toInt() shr 8)
        val y = ((opcode and 0x00F0u).toInt() shr 4)

        val vx = cpu.registers[x]
        val vy = cpu.registers[y]
        val vxVal = vx.read()
        val vyVal = vy.read()

        val noBorrow = if (vxVal >= vyVal) 1u else 0u
        val result = (vxVal - vyVal).toUByte()

        if (x == 0xF) {
            // Vx IS VF → write subtraction result FIRST (so we don't clobber the flag),
            // then write the flag in VF
            vx.write(result)
            cpu.registers[0xF].write(noBorrow.toUByte())
        } else {
            // Regular case: write flag THEN result
            cpu.registers[0xF].write(noBorrow.toUByte())
            vx.write(result)
        }
    }
}