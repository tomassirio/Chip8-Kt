package com.tomassirio.cpu.opcode

import com.tomassirio.cpu.CPU

/**
 * 00EE - RET
 * Return from a subroutine.
 *
 * The interpreter sets the program counter to the address at the top of the stack, then subtracts 1 from the stack pointer.
 *
 */
object RETCommand : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        cpu.pc.write(cpu.stack.pop())
        cpu.sp.value = cpu.sp.value.minus(1u).toUByte()
    }
}