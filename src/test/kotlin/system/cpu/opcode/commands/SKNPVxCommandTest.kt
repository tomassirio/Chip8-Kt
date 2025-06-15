package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.sknpVxCommand
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SKNPVxCommandTest {

    private val command = sknpVxCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `test SKNPVxCommand when key is not pressed`() {
        val opcode: UShort = 0xE0A1u // ExA1
        val vxValue = 0xA.toUByte()

        cpu.registers[0].write(vxValue) // V0 = 'A'

        val previousState = cpu.pc.read()
        command.execute(cpu, opcode)

        // Check that the program counter has been incremented by 2
        assertThat(previousState + 2u).isEqualTo(cpu.pc.read().toUInt())
    }

    @Test
    fun `test SKNPVxCommand when key is pressed`() {
        val opcode: UShort = 0xE09Eu // ExA1
        val vxValue = 0xA.toUByte()

        cpu.registers[0].write(vxValue) // V0 = 'A'
        cpu.keyboardState.pressKey('A') // Simulate key 'A' being pressed

        val previousState = cpu.pc.read()
        command.execute(cpu, opcode)

        // Check that the program counter has not been incremented
        assertThat(previousState).isEqualTo(cpu.pc.read())
    }
}