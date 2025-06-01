package system.cpu.opcode.commands

import com.tomassirio.system.cpu.factory.CPUFactory.createCPU
import com.tomassirio.system.cpu.opcode.commands.jpAddrCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JPAddrCommandTest {
    @Test
    fun testExecute() {
        // Given
        val cpu = createCPU()
        val opcode = 0x1ABC.toUShort()

        // When
        jpAddrCommand().execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(0xABCu.toUShort())
    }
}