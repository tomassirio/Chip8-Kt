package cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.commands.SUBNVxVyCommand
import com.tomassirio.factory.CPUFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SUBNVxVyCommandTest {

    private val command = SUBNVxVyCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `should subtract Vx from Vy and set VF to 0 if there is borrow`() {
        // Given
        val opcode: UShort = 0x8017u.toUShort() // 8XY4
        cpu.registers[0].write(10u.toUByte())
        cpu.registers[1].write(5u.toUByte())

        // When
        command.execute(cpu, opcode)

        // Then
        assertEquals(251.toUByte(), cpu.registers[0].read())
        assertEquals(5.toUByte(), cpu.registers[1].read())
        assertEquals(0.toUByte(), cpu.registers[0xF].read())
    }

    @Test
    fun `should subtract Vx from Vy and set VF to 1 if there is no borrow`() {
        // Given
        val opcode: UShort = 0x8017u.toUShort() // 8XY5
        cpu.registers[0].write(5u.toUByte())
        cpu.registers[1].write(10u.toUByte())

        // When
        command.execute(cpu, opcode)

        // Then
        assertEquals(5.toUByte(), cpu.registers[0].read())
        assertEquals(10.toUByte(), cpu.registers[1].read())
        assertEquals(1.toUByte(), cpu.registers[0xF].read())
    }
}