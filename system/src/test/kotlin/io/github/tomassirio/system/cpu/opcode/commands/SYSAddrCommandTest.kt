package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.CPU
import io.github.tomassirio.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class SYSAddrCommandTest {

    private val sysAddrCommand = sysAddrCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    @Disabled
    fun `should set I register to the address from opcode`() {
        // Given
        val opcode: UShort = 0x0123u.toUShort()

        // When
        sysAddrCommand.execute(cpu, opcode)

        // Then
        assertThat(0x0123u.toUShort()).isEqualTo(cpu.I.read())
            .withFailMessage("I register should contain the address 0x0123 from opcode")
    }

    @Test
    @Disabled
    fun `should ignore upper nibble of opcode`() {
        // Given
        val opcode: UShort = 0xF123u.toUShort()  // Upper nibble should be ignored

        // When
        sysAddrCommand.execute(cpu, opcode)

        // Then
        assertThat(0x0123u.toUShort()).isEqualTo(cpu.I.read())
            .withFailMessage("I register should only contain lower 12 bits of the address")
    }
}