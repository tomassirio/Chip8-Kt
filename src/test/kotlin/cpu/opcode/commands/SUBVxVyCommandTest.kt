package cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.commands.SUBVxVyCommand
import com.tomassirio.factory.CPUFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SUBVxVyCommandTest {

    private val command = SUBVxVyCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `should subtract Vx from Vy and set VF to 1 if there is no borrow`() {
        // Given
        val opcode: UShort = 0x8015u.toUShort() // 8XY4
        cpu.registers[0].write(10u.toUByte())
        cpu.registers[1].write(5u.toUByte())

        // When
        command.execute(cpu, opcode)

        // Then
        assert(cpu.registers[0].read() == 5.toUByte())
        assert(cpu.registers[1].read() == 5.toUByte())
        assert(cpu.registers[0xF].read() == 1.toUByte())
    }

    @Test
    fun `should subtract Vx from Vy and set VF to 0 if there is a borrow`() {
        // Given
        val opcode: UShort = 0x8015u.toUShort() // 8XY5
        cpu.registers[0].write(5u.toUByte())
        cpu.registers[1].write(10u.toUByte())

        // When
        command.execute(cpu, opcode)

        // Then
        assert(cpu.registers[0].read() == 251.toUByte()) // 5 - 10 = -5, which is 251 in unsigned byte
        assert(cpu.registers[1].read() == 10.toUByte())
        assert(cpu.registers[0xF].read() == 0.toUByte())
    }
}