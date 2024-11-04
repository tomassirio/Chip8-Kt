package com.tomassirio.cpu.opcode

import com.tomassirio.cpu.CPU

interface Command {
    fun execute(cpu: CPU, opcode: UShort)
}