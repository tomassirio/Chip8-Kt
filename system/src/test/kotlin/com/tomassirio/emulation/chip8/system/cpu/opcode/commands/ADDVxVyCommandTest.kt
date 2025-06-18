package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory.createCPU
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ADDVxVyCommandTest {
    @Test
    fun `should add Vx and Vy and store the result in Vx`() {
        // Given
        val cpu = createCPU()
        val opcode = 0x8014.toUShort()
        val registerX = cpu.registers[0]
        val registerY = cpu.registers[1]
        registerX.write(0x0Fu)
        registerY.write(0x01u)

        // When
        addVxVyCommand().execute(cpu, opcode)

        // Then
        assertEquals(0x10u.toUByte(), cpu.registers[0].read())
        assertEquals(0x00u.toUByte(), cpu.registers[0xF].read())
    }

    @Test
    fun `ADD surpassing 0xFF should set Flag register to 1 and write lower 8 bits in Vx`() {
        // Given
        val cpu = createCPU()
        val opcode = 0x8014.toUShort()
        val registerX = cpu.registers[0]
        val registerY = cpu.registers[1]
        registerX.write(0xFFu)
        registerY.write(0x01u)

        // When
        addVxVyCommand().execute(cpu, opcode)

        // Then
        assertEquals(0x00u.toUByte(), cpu.registers[0].read())
        assertEquals(0x01u.toUByte(), cpu.registers[0xF].read())
    }
}