package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory.createCPU
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class XORVxVyCommandTest {

    @Test
    fun `should XOR Vx and Vy and store the result in Vx`() {
        // Given
        val cpu = createCPU()
        val opcode = 0x8013.toUShort()

        val registerX = cpu.registers[0]
        val registerY = cpu.registers[1]

        registerX.write(0x0Fu)
        registerY.write(0xFFu)

        // When
        xorVxVyCommand().execute(cpu, opcode)

        // Then
        assertEquals(0xF0u.toUByte(), cpu.registers[0].read())
    }
}