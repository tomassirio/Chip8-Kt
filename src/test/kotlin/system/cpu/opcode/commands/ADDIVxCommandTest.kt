package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.ADDIVxCommand
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ADDIVxCommandTest {
    private val command = ADDIVxCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `test ADDIVxCommand adds value of Register X to I`() {
        // Arrange
        val initialI = 0x100u.toUShort()
        val value = 0x42u.toUByte()
        val opcode = 0xF01Eu.toUShort() // ADD I, Vx where x = 0
        cpu.I.write(initialI)
        cpu.registers[0].write(value)
        val expectedValue = 0x142u.toUShort()

        // Act
        command.execute(cpu, opcode)

        // Assert
        assertThat(cpu.I.read()).isEqualTo(expectedValue)
    }

}