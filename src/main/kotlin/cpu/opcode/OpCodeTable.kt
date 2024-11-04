package com.tomassirio.cpu.opcode

class OpcodeTable {
    fun getCommand(opcode: UShort): Command {
        return when (opcode.and(0xFFFu).toUInt()) {
            0x0000u -> SysAddrCommand
            else -> throw IllegalArgumentException("Unknown opcode: ${opcode.toString(16)}")
        }
    }
}