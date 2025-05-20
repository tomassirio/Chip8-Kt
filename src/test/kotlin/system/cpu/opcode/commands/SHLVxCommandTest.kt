package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.commands.SHLVxCommand
import com.tomassirio.system.cpu.factory.CPUFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SHLVxCommandTest {

    private val command = SHLVxCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun testSHLCommand_whenNoCarryNeeded() {
        val opcode: UShort = 0x801E.toUShort() // 8XYE

        cpu.registers[0].write(0b01100110.toUByte()) // V0 = 01100110
        cpu.registers[1].write(0b00000001.toUByte()) // V1 = 00000001 (unchanged)

        // Execute the SHL command
        command.execute(cpu, opcode)

        // Check the result
        assert(cpu.registers[0].read() == 0b11001100.toUByte()) // V0 should be 11001100
        assert(cpu.registers[1].read() == 0b00000001.toUByte()) // V1 should remain unchanged
        assert(cpu.registers[0xF].read() == 0b00000000.toUByte()) // VF should be 0 (no carry)
    }

    @Test
    fun testSHLCommand_whenCarryNeeded() {
        val opcode: UShort = 0x801E.toUShort() // 8XYE

        cpu.registers[0].write(0b11111111.toUByte()) // V0 = 11111111
        cpu.registers[1].write(0b00000001.toUByte()) // V1 = 00000001 (unchanged)

        // Execute the SHL command
        command.execute(cpu, opcode)

        // Check the result
        assert(cpu.registers[0].read() == 0b11111110.toUByte()) // V0 should be 11111110
        assert(cpu.registers[1].read() == 0b00000001.toUByte()) // V1 should remain unchanged
        assert(cpu.registers[0xF].read() == 0b00000001.toUByte()) // VF should be 1 (carry)
    }
}