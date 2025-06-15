package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.drwVxVyNCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DRWVxVyNCommandTest {

    private val command = drwVxVyNCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `should draw sprite at the specified coordinates`() {
        // Given
        val opcode: UShort = 0xD123u // Draw 3-byte sprite at (V1, V2)
        cpu.registers[1].write(5u) // V1 = 5 (X coordinate)
        cpu.registers[2].write(10u) // V2 = 10 (Y coordinate)
        cpu.I.write(0x200u) // I register points to the sprite data in memory

        // Setup sprite data in memory - 3 bytes representing:
        // Byte 1: 10010000 (0x90) - Pixels at positions 0 and 3
        // Byte 2: 10100000 (0xA0) - Pixels at positions 0 and 2
        // Byte 3: 11110000 (0xF0) - Pixels at positions 0, 1, 2, and 3
        cpu.memory.writeByte(0x200, 0x90u)
        cpu.memory.writeByte(0x201, 0xA0u)
        cpu.memory.writeByte(0x202, 0xF0u)

        // When
        command.execute(cpu, opcode)

        // Then - Check expected pattern (MSB first)
        // Row 1 (y=10): Pixels at (5,10) and (8,10) should be set
        assertThat(cpu.displayState.getPixel(5, 10)).isTrue()  // Bit 0 of 0x90
        assertThat(cpu.displayState.getPixel(6, 10)).isFalse() // Bit 1 of 0x90
        assertThat(cpu.displayState.getPixel(7, 10)).isFalse() // Bit 2 of 0x90
        assertThat(cpu.displayState.getPixel(8, 10)).isTrue()  // Bit 3 of 0x90
        assertThat(cpu.displayState.getPixel(9, 10)).isFalse() // Bit 4 of 0x90

        // Row 2 (y=11): Pixels at (5,11) and (7,11) should be set
        assertThat(cpu.displayState.getPixel(5, 11)).isTrue()  // Bit 0 of 0xA0
        assertThat(cpu.displayState.getPixel(6, 11)).isFalse() // Bit 1 of 0xA0
        assertThat(cpu.displayState.getPixel(7, 11)).isTrue()  // Bit 2 of 0xA0
        assertThat(cpu.displayState.getPixel(8, 11)).isFalse() // Bit 3 of 0xA0

        // Row 3 (y=12): Pixels at (5,12), (6,12), (7,12), and (8,12) should be set
        assertThat(cpu.displayState.getPixel(5, 12)).isTrue()  // Bit 0 of 0xF0
        assertThat(cpu.displayState.getPixel(6, 12)).isTrue()  // Bit 1 of 0xF0
        assertThat(cpu.displayState.getPixel(7, 12)).isTrue()  // Bit 2 of 0xF0
        assertThat(cpu.displayState.getPixel(8, 12)).isTrue()  // Bit 3 of 0xF0
        assertThat(cpu.displayState.getPixel(9, 12)).isFalse() // Bit 4 of 0xF0

        // VF should be 0 since there were no collisions
        assertThat(cpu.registers[0xF].read()).isEqualTo(0u.toUByte())
    }

    @Test
    fun `should set VF to 1 when collision occurs`() {
        // Given
        val opcode: UShort = 0xD121u // Draw 1-byte sprite at (V1, V2)
        cpu.registers[1].write(5u) // V1 = 5 (X coordinate)
        cpu.registers[2].write(10u) // V2 = 10 (Y coordinate)
        cpu.I.write(0x200u) // I register points to the sprite data in memory

        // First, set some pixels on the screen
        cpu.displayState.setPixel(5, 10, true)

        // Setup sprite data with a pattern that will collide
        cpu.memory.writeByte(0x200, 0x80u.toUByte()) // Just the leftmost pixel

        // When
        command.execute(cpu, opcode)

        // Then
        // The pixel at (5,10) should now be off (XOR with 1)
        assertThat(cpu.displayState.getPixel(5, 10)).isFalse()

        // VF should be 1 because a collision occurred
        assertThat(cpu.registers[0xF].read()).isEqualTo(1u.toUByte())
    }

    @Test
    fun `should wrap sprite at screen edges`() {
        // Given
        val opcode: UShort = 0xD121u // Draw 1-byte sprite at (V1, V2)
        cpu.registers[1].write(63u) // V1 = 63 (X at right edge - assuming 64-width displayState)
        cpu.registers[2].write(10u) // V2 = 10 (Y coordinate)
        cpu.I.write(0x200u) // I register points to the sprite data in memory

        // Setup sprite data that will wrap
        cpu.memory.writeByte(0x200, 0xC0u) // Two leftmost pixels set

        // When
        command.execute(cpu, opcode)

        // Then
        // Check last pixel of the row and first pixel of the next row (wrapped)
        assertThat(cpu.displayState.getPixel(63, 10)).isTrue()
        assertThat(cpu.displayState.getPixel(0, 10)).isTrue()
    }
}