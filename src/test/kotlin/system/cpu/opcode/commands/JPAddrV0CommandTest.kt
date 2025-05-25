package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.jpAddrV0Command
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JPAddrV0CommandTest {

    private val command = jpAddrV0Command()
    private lateinit var cpu: CPU

    @Test
    fun testJPAddrV0Command() {
        // Given
        cpu = CPUFactory.createCPU()
        val opcode: UShort = 0xB123u // B123
        cpu.registers[0].write(0x10u) // V0 = 0x10
        cpu.pc.write(0x200u) // PC = 0x200

        // When
        command.execute(cpu, opcode)

        // Then
        assertEquals(cpu.pc.read().toUInt(), 0x133u) // PC should be set to 0x123
    }
}