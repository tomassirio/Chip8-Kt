package cpu.opcode.commands

import com.tomassirio.cpu.CPUFactory.createCPU
import com.tomassirio.cpu.opcode.commands.JPAddrCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JPAddrCommandTest {
    @Test
    fun testExecute() {
        // Given
        val cpu = createCPU()
        val opcode = 0x1ABC.toUShort()

        // When
        JPAddrCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.value).isEqualTo(0xABCu.toUShort())
    }
}