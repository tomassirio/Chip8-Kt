package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.CPU
import com.tomassirio.emulation.chip8.system.cpu.CPUType
import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LDHFVxCommandKtTest {
    private val command = ldhfVxCommand()
    private lateinit var cpu: CPU

    @Test
    fun testLdhfVxCommand() {
        // Given
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
        val opcode: UShort = 0xF130u
        cpu.registers[1].write(5u)

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.I.read()).isEqualTo(50u.toUShort())
    }
}