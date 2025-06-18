package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory.createCPU
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CALLAddrCommandTest {
    @Test
    fun testExecute() {
        // Given
        val cpu = createCPU()
        val opcode = 0x2ABC.toUShort()
        val previousSP = cpu.stack.lastIndex

        // When
        callAddrCommand().execute(cpu, opcode)

        // Then
        assertThat(cpu.stack.lastIndex).isEqualTo(previousSP.plus(1))
        assertThat(cpu.pc.read()).isEqualTo(0xABCu.toUShort())
        assertThat(cpu.stack.pop()).isEqualTo(0x202u.toUShort())
    }
}