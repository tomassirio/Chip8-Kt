package cpu.opcode.commands

import com.tomassirio.cpu.opcode.commands.SNEVxByteCommand
import com.tomassirio.factory.CPUFactory.createCPU
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SNEVxByteCommandTest {
    @Test
    fun `execute should skip next instruction if Vx != kk`() {
        // Given
        val cpu = createCPU()
        val register = cpu.registers["0"]
        register.write(0x01u)
        cpu.pc.write(0x200u)
        val opcode = 0x4010u.toUShort()

        // When
        SNEVxByteCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0x202u.toUShort())
    }

    @Test
    fun `execute should not skip next instruction if Vx == kk`() {
        // Given
        val cpu = createCPU()
        val register = cpu.registers["0"]
        register.write(0x11u)
        cpu.pc.write(0x200u)
        val opcode = 0x4011u.toUShort()

        // When
        SNEVxByteCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0x200u.toUShort())
    }
}