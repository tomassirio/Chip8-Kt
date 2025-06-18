package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory.createCPU
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ORVxVyCommandTest {
    @Test
    fun `should perform bitwise OR between Vx and Vy and store the result in Vx`() {
        // Given
        val cpu = createCPU()
        val opcode = 0x8011.toUShort()
        val registerX = cpu.registers[0]
        val registerY = cpu.registers[1]
        registerX.write(0x0Fu)
        registerY.write(0xF0u)

        // When
        orVxVyCommand().execute(cpu, opcode)

        // Then
        assertThat(cpu.registers[0].read()).isEqualTo(0xFFu.toUByte())
    }
}