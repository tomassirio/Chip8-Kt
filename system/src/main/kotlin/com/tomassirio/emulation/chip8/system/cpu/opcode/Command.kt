package com.tomassirio.emulation.chip8.system.cpu.opcode

import com.tomassirio.emulation.chip8.system.cpu.CPU

open class Command(
    val skipsPcIncrement: Boolean = false,
    val execute: (cpu: CPU, opcode: UShort) -> Unit
) {
    operator fun invoke(cpu: CPU, opcode: UShort) {
        execute(cpu, opcode)
    }
}