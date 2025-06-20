package io.github.tomassirio.system.cpu.opcode

import io.github.tomassirio.system.cpu.CPU

open class Command(
    val skipsPcIncrement: Boolean = false,
    val execute: (cpu: CPU, opcode: UShort) -> Unit
) {
    operator fun invoke(cpu: CPU, opcode: UShort) {
        execute(cpu, opcode)
    }
}