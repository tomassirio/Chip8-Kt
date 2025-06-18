package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory.createCPU
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JPAddrCommandTest {
    @Test
    fun testExecute() {
        // Given
        val cpu = createCPU()
        val opcode = 0x1ABC.toUShort()

        // When
        jpAddrCommand().execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0xABCu.toUShort())
    }
}