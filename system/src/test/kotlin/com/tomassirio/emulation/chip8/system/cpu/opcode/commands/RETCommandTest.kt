package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory.createCPU
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RETCommandTest {
    @Test
    fun testExecute() {
        // Given
        val cpu = createCPU()
        cpu.stack.push(0x1234u)
        cpu.stack.push(0x5678u)
        cpu.stack.push(0x9abcu)
        val opcode = 0x00EE.toUShort()

        // When
        retCommand().execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0x9abcu.toUShort())
        assertThat(cpu.stack.lastIndex).isEqualTo(1)
        assertThat(cpu.stack.lastElement()).isEqualTo(0x5678u.toUShort())
    }
}