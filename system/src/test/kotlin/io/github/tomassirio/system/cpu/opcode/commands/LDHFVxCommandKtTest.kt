package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.CPU
import io.github.tomassirio.system.cpu.CPUType
import io.github.tomassirio.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LDHFVxCommandKtTest {
    private val command = ldhfVxCommand()
    private lateinit var cpu: CPU

    @Test
    fun testLdhfVxCommand() {
        // Given
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
        val opcode: UShort = 0xF130u
        cpu.registers[1].write(5u)

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.I.read()).isEqualTo(50u.toUShort())
    }
}