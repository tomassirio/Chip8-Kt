package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.CPU
import io.github.tomassirio.system.cpu.factory.CPUFactory
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LDVxICommandKtTest {
    private val ldVxICommand = ldVxICommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `test ldVxICommand loads single register V0 from memory`() {
        // Given
        val memoryAddress = 0x300.toUShort()
        val opcode = 0xF065.toUShort() // F065 - LD V0, [I]

        cpu.I.write(memoryAddress)
        cpu.memory.writeByte(memoryAddress.toInt(), 0x99.toUByte())

        // When
        ldVxICommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.registers[0].read()).isEqualTo(0x99.toUByte())
    }

    @Test
    fun `test ldVxICommand loads multiple registers V0 through V4 from memory`() {
        // Given
        val memoryAddress = 0x400.toUShort()
        val opcode = 0xF465.toUShort() // F465 - LD V4, [I]

        cpu.I.write(memoryAddress)
        cpu.memory.writeByte(memoryAddress.toInt(), 0x11.toUByte())
        cpu.memory.writeByte(memoryAddress.toInt() + 1, 0x22.toUByte())
        cpu.memory.writeByte(memoryAddress.toInt() + 2, 0x33.toUByte())
        cpu.memory.writeByte(memoryAddress.toInt() + 3, 0x44.toUByte())
        cpu.memory.writeByte(memoryAddress.toInt() + 4, 0x55.toUByte())
        cpu.memory.writeByte(memoryAddress.toInt() + 5, 0x66.toUByte()) // Should not be loaded

        // When
        ldVxICommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.registers[0].read()).isEqualTo(0x11.toUByte())
        assertThat(cpu.registers[1].read()).isEqualTo(0x22.toUByte())
        assertThat(cpu.registers[2].read()).isEqualTo(0x33.toUByte())
        assertThat(cpu.registers[3].read()).isEqualTo(0x44.toUByte())
        assertThat(cpu.registers[4].read()).isEqualTo(0x55.toUByte())
        // V5 should remain 0 (not loaded)
        assertThat(cpu.registers[5].read()).isEqualTo(0x00.toUByte())
    }

    @Test
    fun `test ldVxICommand overwrites existing register values`() {
        // Given
        val memoryAddress = 0x500.toUShort()
        val opcode = 0xF265.toUShort() // F265 - LD V2, [I]

        // Pre-populate registers with different values
        cpu.registers[0].write(0xFF.toUByte())
        cpu.registers[1].write(0xEE.toUByte())
        cpu.registers[2].write(0xDD.toUByte())

        // Set up memory
        cpu.I.write(memoryAddress)
        cpu.memory.writeByte(memoryAddress.toInt(), 0x01.toUByte())
        cpu.memory.writeByte(memoryAddress.toInt() + 1, 0x02.toUByte())
        cpu.memory.writeByte(memoryAddress.toInt() + 2, 0x03.toUByte())

        // When
        ldVxICommand.execute(cpu, opcode)

        // Then - registers should be overwritten
        assertThat(cpu.registers[0].read()).isEqualTo(0x01.toUByte())
        assertThat(cpu.registers[1].read()).isEqualTo(0x02.toUByte())
        assertThat(cpu.registers[2].read()).isEqualTo(0x03.toUByte())
    }
}