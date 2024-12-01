package cpu.opcode.commands

import com.tomassirio.factory.CPUFactory.createCPU
import com.tomassirio.cpu.opcode.commands.RETCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RETCommandTest {
    @Test
    fun testExecute() {
        // Given
        val cpu = createCPU()
        cpu.stack.push(0x1234u)
        cpu.stack.push(0x5678u)
        cpu.stack.push(0x9abcu)
        cpu.sp.write(0x2u)
        val opcode = 0x00EE.toUShort()

        // When
        RETCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0x9abcu.toUShort())
        assertThat(cpu.sp.read()).isEqualTo(0x1u.toUByte())
    }
}