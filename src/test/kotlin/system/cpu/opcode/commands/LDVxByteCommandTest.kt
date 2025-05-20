package system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.commands.LDVxByteCommand
import com.tomassirio.system.cpu.factory.CPUFactory.createCPU
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LDVxByteCommandTest {

    @Test
    fun `execute should set Vx to kk`() {
        // Given
        val cpu = createCPU()
        val register = cpu.registers[0]
        val opcode = 0x6069u.toUShort()

        // When
        LDVxByteCommand.execute(cpu, opcode)

        // Then
        assertThat(register.read()).isEqualTo(0x69u.toUByte())
    }
}