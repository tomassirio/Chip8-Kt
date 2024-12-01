package cpu.opcode.commands

import com.tomassirio.cpu.CPUFactory.createCPU
import com.tomassirio.cpu.opcode.commands.CALLAddrCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CALLAddrCommandTest {
    @Test
    fun testExecute() {
        // Given
        val cpu = createCPU()
        val opcode = 0x2ABC.toUShort()
        val previousSP = cpu.sp.value

        // When
        CALLAddrCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.sp.value).isEqualTo(previousSP.plus(0x1u).toUByte())
        assertThat(cpu.pc.value).isEqualTo(0xABCu.toUShort())
        assertThat(cpu.stack.pop()).isEqualTo(0x200u.toUShort())
    }
}