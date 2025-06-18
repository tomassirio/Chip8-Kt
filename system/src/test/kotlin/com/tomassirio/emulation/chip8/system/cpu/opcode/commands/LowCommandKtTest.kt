package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.CPU
import com.tomassirio.emulation.chip8.system.cpu.CPUType
import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LowCommandKtTest {
    private val command = lowCommand()
    private lateinit var cpu: CPU

    @Test
    fun testLowhCommand() {
        // Given
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
        val opcode: UShort = 0x00FEu
        cpu.displayState.enableExtendedMode()

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.displayState.isExtended()).isFalse()
    }
}