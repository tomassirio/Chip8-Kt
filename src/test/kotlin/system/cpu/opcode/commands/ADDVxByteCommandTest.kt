package system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.commands.ADDVxByteCommand
import com.tomassirio.system.cpu.factory.CPUFactory.createCPU
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ADDVxByteCommandTest {

    @Test
    fun `execute should add kk to Vx`() {
        // Given
        val cpu = createCPU()
        val register = cpu.registers[0]
        register.write(0x01u)
        val opcode = 0x7001u.toUShort()

        // When
        ADDVxByteCommand.execute(cpu, opcode)

        // Then
        assertEquals(0x02u.toUByte(), register.read())
    }
}