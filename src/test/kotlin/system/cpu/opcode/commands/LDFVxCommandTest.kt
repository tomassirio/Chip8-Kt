package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.LDFVxCommand
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LDFVxCommandTest {

    private val command = LDFVxCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU()
    }

    @ParameterizedTest
    @ValueSource(ints = [0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF])
    fun `test LDFVxCommand sets I to correct sprite address for each digit`(digit: Int) {
        // Arrange
        cpu.registers[0].write(digit.toUByte())
        val opcode = 0xF029u.toUShort() // Fx29 where x = 0
        val expectedAddress = (digit * 5).toUShort()

        // Act
        command.execute(cpu, opcode)

        // Assert
        assertThat(cpu.I.read()).isEqualTo(expectedAddress)
    }
}