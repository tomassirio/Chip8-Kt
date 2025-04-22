package cpu.opcode.commands

import com.tomassirio.cpu.opcode.commands.LDIToAddrCommand
import com.tomassirio.factory.CPUFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LDIToAddrCommandTest {
    private val command = LDIToAddrCommand

    @Test
    fun testLDIToAddrCommand() {
        // Given
        val cpu = CPUFactory.createCPU()
        val opcode: UShort = 0xA123u // A123

        // When
        command.execute(cpu, opcode)

        // Then
        assertEquals(0x123u, cpu.I.read().toUInt()) // I should be set to 0x123
    }
 }