package system.cpu.opcode.commands

import com.tomassirio.system.cpu.factory.CPUFactory.createCPU
import com.tomassirio.system.cpu.opcode.commands.andVxVyCommand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ANDVxVyCommandTest {

    @Test
    fun `should perform bitwise AND between Vx and Vy and store the result in Vx`() {
        // Given
        val cpu = createCPU()
        val opcode = 0x8012.toUShort()
        val registerX = cpu.registers[0]
        val registerY = cpu.registers[1]
        registerX.write(0x0Fu)
        registerY.write(0xFFu)

        // When
        andVxVyCommand().execute(cpu, opcode)

        // Then
        assertEquals(0x0Fu.toUByte(), cpu.registers[0].read())
    }
}