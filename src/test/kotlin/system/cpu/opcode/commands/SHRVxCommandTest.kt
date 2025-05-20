package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.commands.SHRVxCommand
import com.tomassirio.system.cpu.factory.CPUFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SHRVxCommandTest {
    private val command = SHRVxCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun testSHRCommand_whenNoCarryNeeded() {
        val opcode: UShort = 0x8016u // 8XY6

        cpu.registers[0].write(0b11001100.toUByte()) // V0 = 11001100
        cpu.registers[1].write(0b00000001.toUByte()) // V1 = 00000001 (unchanged)

        // Execute the SHR command
        command.execute(cpu, opcode)

        // Check the result
        assertEquals(0b01100110.toUByte(), cpu.registers[0].read()) // V0 should be 01100110
        assertEquals(0b00000001.toUByte(), cpu.registers[1].read()) // V1 should remain unchanged
        assertEquals(0b00000000.toUByte(), cpu.registers[0xF].read()) // VF should be 0 (no carry)
    }

    @Test
    fun testSHRCommand_whenCarryNeeded() {
        val opcode: UShort = 0x8016u // 8XY6

        cpu.registers[0].write(0b11001101.toUByte()) // V0 = 11001101
        cpu.registers[1].write(0b00000001.toUByte()) // V1 = 00000001 (unchanged)

        // Execute the SHR command
        command.execute(cpu, opcode)

        // Check the result
        assertEquals(0b01100110.toUByte(), cpu.registers[0].read()) // V0 should be 01100110
        assertEquals(0b00000001.toUByte(), cpu.registers[1].read()) // V1 should remain unchanged
        assertEquals(0b00000001.toUByte(), cpu.registers[0xF].read()) // VF should be 1 (carry)
    }
}