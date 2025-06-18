package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.CPU
import com.tomassirio.emulation.chip8.system.cpu.CPUType
import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LDRVxCommandKtTest {
    private val command = ldRVxCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testLdRVxCommand() {
        // Given
        val opcode: UShort = 0xF375u
        cpu.registers[0].write(0xA0u)
        cpu.registers[1].write(0xB1u)
        cpu.registers[2].write(0xC2u)
        cpu.registers[3].write(0xD3u)

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.rplFlags).containsSequence(0xA0u, 0xB1u, 0xC2u, 0xD3u)
    }
}