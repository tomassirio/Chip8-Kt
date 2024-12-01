package cpu.opcode.commands

import com.tomassirio.factory.CPUFactory.createCPU
import com.tomassirio.cpu.opcode.commands.CALLAddrCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CALLAddrCommandTest {
    @Test
    fun testExecute() {
        // Given
        val cpu = createCPU()
        val opcode = 0x2ABC.toUShort()
        val previousSP = cpu.sp.read()

        // When
        CALLAddrCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.sp.read()).isEqualTo(previousSP.plus(0x1u).toUByte())
        assertThat(cpu.pc.read()).isEqualTo(0xABCu.toUShort())
        assertThat(cpu.stack.pop()).isEqualTo(0x200u.toUShort())
    }
}