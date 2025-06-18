package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory.createCPU
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SEVxVyCommandTest {

    @Test
    fun `execute should skip next instruction if Vx == Vy`() {
        // Given
        val cpu = createCPU()
        val registerX = cpu.registers[0]
        val registerY = cpu.registers[1]
        registerX.write(0x01u)
        registerY.write(0x01u)
        cpu.pc.write(0x200u)
        val opcode = 0x5010u.toUShort()

        // When
        seVxVyCommand().execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0x202u.toUShort())
    }

    @Test
    fun `execute should not skip next instruction if Vx != Vy`() {
        // Given
        val cpu = createCPU()
        val registerX = cpu.registers[0]
        val registerY = cpu.registers[1]
        registerX.write(0x01u)
        registerY.write(0x02u)
        cpu.pc.write(0x200u)
        val opcode = 0x5010u.toUShort()

        // When
        seVxVyCommand().execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0x200u.toUShort())
    }
}