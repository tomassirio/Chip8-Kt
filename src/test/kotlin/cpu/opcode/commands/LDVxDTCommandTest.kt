package cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.commands.LDVxDTCommand
import com.tomassirio.factory.CPUFactory
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LDVxDTCommandTest {
    private val command = LDVxDTCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `test LDVxDTCommand sets Vx to DT value`() {
        val expectedValue = 0xAB
        cpu.DT.write(expectedValue.toUByte())
        val opcode = 0xF007.toUShort()

        command.execute(cpu, opcode)

        val registerX = cpu.registers[0]
        assertThat(expectedValue.toUByte()).isEqualTo(registerX.read())
    }
}