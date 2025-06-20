package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.CPU
import io.github.tomassirio.system.cpu.CPUType
import io.github.tomassirio.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LDVxRCommandKtTest {
    private val command = ldVxRCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testLdVxRCommand() {
        // Given
        val opcode: UShort = 0xF385u // FX85, X = 3
        cpu.rplFlags!![0] = 0xA0u
        cpu.rplFlags!![1] = 0xB1u
        cpu.rplFlags!![2] = 0xC2u
        cpu.rplFlags!![3] = 0xD3u

        for (i in 0..3) {
            cpu.registers[i].write(0u)
        }

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.registers[0].read()).isEqualTo(0xA0u.toUByte())
        assertThat(cpu.registers[1].read()).isEqualTo(0xB1u.toUByte())
        assertThat(cpu.registers[2].read()).isEqualTo(0xC2u.toUByte())
        assertThat(cpu.registers[3].read()).isEqualTo(0xD3u.toUByte())
    }
}
