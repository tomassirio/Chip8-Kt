package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.CPU
import io.github.tomassirio.system.cpu.factory.CPUFactory
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LDDTVxCommandTest {
    private val command = ldDTVxCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `test LDDTVxCommand sets DT with Register X's value`() {
        // Arrange
        val value = 0x42u.toUByte()
        val opcode = 0xF015u.toUShort()
        cpu.registers[0].write(value)

        // Act
        command.execute(cpu, opcode)

        // Assert
        assertThat(cpu.DT.read()).isEqualTo(value)
    }

}