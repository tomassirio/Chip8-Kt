package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.CPU
import com.tomassirio.emulation.chip8.system.cpu.CPUType
import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HighCommandKtTest {
    private val command = highCommand()
    private lateinit var cpu: CPU

    @Test
    fun testHighCommand() {
        // Given
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
        val opcode: UShort = 0x00FFu // B123

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.displayState.isExtended()).isTrue()
    }
}