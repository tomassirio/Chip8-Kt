package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LDVxVyCommandTest {
        @Test
        fun `execute should set Vx to Vy`() {
            // Given
            val cpu = CPUFactory.createCPU()
            val registerX = cpu.registers[0]
            val registerY = cpu.registers[1]
            registerY.write(0x69u)
            val opcode = 0x8010u.toUShort()

            // When
            ldVxVyCommand().execute(cpu, opcode)

            // Then
            assertThat(registerX.read()).isEqualTo(0x69u.toUByte())
        }
}