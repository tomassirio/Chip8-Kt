package cpu.opcode.commands

import com.tomassirio.cpu.opcode.commands.LDVxVyCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LDVxVyCommandTest {
        @Test
        fun `execute should set Vx to Vy`() {
            // Given
            val cpu = com.tomassirio.factory.CPUFactory.createCPU()
            val registerX = cpu.registers["0"]
            val registerY = cpu.registers["1"]
            registerY.write(0x69u)
            val opcode = 0x8010u.toUShort()

            // When
            LDVxVyCommand.execute(cpu, opcode)

            // Then
            assertThat(registerX.read()).isEqualTo(0x69u.toUByte())
        }
}