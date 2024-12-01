package cpu.opcode.commands

import com.tomassirio.factory.CPUFactory.createCPU
import com.tomassirio.cpu.opcode.commands.SEVxByteCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SEVxByteCommandTest {

    @Test
    fun testExecute() {
        // Given
        val cpu = createCPU()
        cpu.registers["0"].write(0x12u)
        val opcode = 0x3012u.toUShort()

        val previousPC = cpu.pc.read()

        // When
        SEVxByteCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo((previousPC + 2u).toUShort())
    }

    @Test
    fun `should do nothing if register value is not equal to the value in the opcode`() {
        // Given
        val cpu = createCPU()
        cpu.registers["0"].write(0x12u)
        val opcode = 0x3013u.toUShort()

        val previousPC = cpu.pc.read()

        // When
        SEVxByteCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.pc.read()).isEqualTo(previousPC)
    }

}