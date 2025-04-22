package cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.commands.SNEVxVyCommand
import com.tomassirio.factory.CPUFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SNEVxVyCommandTest {
    private val command = SNEVxVyCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun testSNEVxVyCommand_whenRegistersAreEqual() {
        val opcode: UShort = 0x9010u // 9XY0

        cpu.registers[0].write(5u.toUByte()) // V0 = 5
        cpu.registers[1].write(5u.toUByte()) // V1 = 5

        // Execute the SNE command
        command.execute(cpu, opcode)

        // Check the result
        assertEquals(0x200u, cpu.pc.read().toUInt()) // PC should not increment (0x200)
    }

    @Test
    fun testSNEVxVyCommand_whenRegistersAreNotEqual() {
        val opcode: UShort = 0x9010u // 9XY0

        cpu.registers[0].write(5u.toUByte()) // V0 = 5
        cpu.registers[1].write(10u.toUByte()) // V1 = 10

        // Execute the SNE command
        command.execute(cpu, opcode)

        // Check the result
        assertEquals(0x202u, cpu.pc.read().toUInt()) // PC should increment by 2 (0x200 + 2)
    }

}