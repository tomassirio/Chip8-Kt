package com.tomassirio.system.cpu.opcode

import com.tomassirio.system.cpu.CPU

fun interface Command {
    fun execute(cpu: CPU, opcode: UShort)
}