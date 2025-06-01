package system.cpu.opcode.commands

import com.tomassirio.system.cpu.factory.CPUFactory.createCPU
import com.tomassirio.system.cpu.opcode.commands.sneVxByteCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SNEVxByteCommandTest {
    @Test
    fun `execute should skip next instruction if Vx != kk`() {
        // Given
        val cpu = createCPU()
        val register = cpu.registers[0]
        register.write(0x01u)
        cpu.pc.write(0x200u)
        val opcode = 0x4010u.toUShort()

        // When
        sneVxByteCommand().execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0x202u.toUShort())
    }

    @Test
    fun `execute should not skip next instruction if Vx == kk`() {
        // Given
        val cpu = createCPU()
        val register = cpu.registers[0]
        register.write(0x11u)
        cpu.pc.write(0x200u)
        val opcode = 0x4011u.toUShort()

        // When
        sneVxByteCommand().execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0x200u.toUShort())
    }
}