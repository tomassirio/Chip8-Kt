package system.cpu.opcode.commands

import com.tomassirio.system.cpu.opcode.commands.RNDVxByteCommand
import com.tomassirio.system.cpu.factory.CPUFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class RNDVxByteCommandTest {

    private val fixedRandomValue = 0b10101010

    @Test
    fun testRNDVxByteCommand() {
        // Given
        val fixedRandom = object : Random() {
            override fun nextBits(bitCount: Int): Int = fixedRandomValue
        }
        val command = RNDVxByteCommand(fixedRandom)
        val cpu = CPUFactory.createCPU()
        val opcode: UShort = 0xC123u // C123
        val mask: UByte = 0x23u      // Lower 8 bits of the opcode

        // When
        command.execute(cpu, opcode)

        // Then
        val result = cpu.registers[1].read()
        assertEquals(fixedRandomValue.toUByte() and mask, result)
    }
}