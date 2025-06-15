package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.ldBVxCommand
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class LDBVXCommandKtTest {
    private val command = ldBVxCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `test ldBVxCommand sets BCD representation of Vx in memory`() {
        // Given
        val registerIndex = 5
        val value = 234.toUByte()
        val memoryAddress = 0x300.toUShort()
        val opcode = (0xF533).toUShort() // F533 - LD B, V5

        // Set up CPU state
        cpu.registers[registerIndex].write(value)
        cpu.I.write(memoryAddress)

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.memory.readByte(memoryAddress.toInt())).isEqualTo(2.toUByte()) // hundreds
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 1)).isEqualTo(3.toUByte()) // tens
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 2)).isEqualTo(4.toUByte()) // ones
    }

    @ParameterizedTest
    @MethodSource("bcdTestCases")
    fun `test ldBVxCommand handles various BCD values correctly`(
        registerValue: Int,
        expectedHundreds: Int,
        expectedTens: Int,
        expectedOnes: Int
    ) {
        // Given
        val registerIndex = 0xA
        val memoryAddress = 0x200.toUShort()
        val opcode = (0xFA33).toUShort() // FA33 - LD B, VA

        // Set up CPU state
        cpu.registers[registerIndex].write(registerValue.toUByte())
        cpu.I.write(memoryAddress)

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.memory.readByte(memoryAddress.toInt())).isEqualTo(expectedHundreds.toUByte())
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 1)).isEqualTo(expectedTens.toUByte())
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 2)).isEqualTo(expectedOnes.toUByte())
    }

    @Test
    fun `test ldBVxCommand extracts correct register from opcode`() {
        // Given
        val memoryAddress = 0x400.toUShort()
        cpu.I.write(memoryAddress)

        // Set different values in different registers
        cpu.registers[0x7].write(123.toUByte())
        cpu.registers[0xC].write(89.toUByte())

        // Test register V7
        val opcodeV7 = (0xF733).toUShort() // F733 - LD B, V7
        command.execute(cpu, opcodeV7)

        assertThat(cpu.memory.readByte(memoryAddress.toInt())).isEqualTo(1.toUByte()) // 123 -> hundreds = 1
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 1)).isEqualTo(2.toUByte()) // tens = 2
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 2)).isEqualTo(3.toUByte()) // ones = 3

        // Test register VC
        val opcodeVC = (0xFC33).toUShort() // FC33 - LD B, VC
        command.execute(cpu, opcodeVC)

        assertThat(cpu.memory.readByte(memoryAddress.toInt())).isEqualTo(0.toUByte()) // 89 -> hundreds = 0
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 1)).isEqualTo(8.toUByte()) // tens = 8
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 2)).isEqualTo(9.toUByte()) // ones = 9
    }

    @Test
    fun `test ldBVxCommand uses I register for memory address`() {
        // Given
        val registerIndex = 3
        val value = 42.toUByte()
        val memoryAddress1 = 0x500.toUShort()
        val memoryAddress2 = 0x600.toUShort()
        val opcode = (0xF333).toUShort() // F333 - LD B, V3

        cpu.registers[registerIndex].write(value)

        // Test with first memory address
        cpu.I.write(memoryAddress1)
        command.execute(cpu, opcode)

        assertThat(cpu.memory.readByte(memoryAddress1.toInt())).isEqualTo(0.toUByte()) // hundreds
        assertThat(cpu.memory.readByte(memoryAddress1.toInt() + 1)).isEqualTo(4.toUByte()) // tens
        assertThat(cpu.memory.readByte(memoryAddress1.toInt() + 2)).isEqualTo(2.toUByte()) // ones

        // Test with second memory address
        cpu.I.write(memoryAddress2)
        command.execute(cpu, opcode)

        assertThat(cpu.memory.readByte(memoryAddress2.toInt())).isEqualTo(0.toUByte()) // hundreds
        assertThat(cpu.memory.readByte(memoryAddress2.toInt() + 1)).isEqualTo(4.toUByte()) // tens
        assertThat(cpu.memory.readByte(memoryAddress2.toInt() + 2)).isEqualTo(2.toUByte()) // ones

        // Verify first memory location is unchanged
        assertThat(cpu.memory.readByte(memoryAddress1.toInt())).isEqualTo(0.toUByte())
    }

    @Test
    fun `test ldBVxCommand overwrites existing memory values`() {
        // Given
        val registerIndex = 1
        val value = 15.toUByte()
        val memoryAddress = 0x300.toUShort()
        val opcode = (0xF133).toUShort() // F133 - LD B, V1

        // Pre-populate memory with different values
        cpu.memory.writeByte(memoryAddress.toInt(), 9.toUByte())
        cpu.memory.writeByte(memoryAddress.toInt() + 1, 8.toUByte())
        cpu.memory.writeByte(memoryAddress.toInt() + 2, 7.toUByte())

        cpu.registers[registerIndex].write(value)
        cpu.I.write(memoryAddress)

        // When
        command.execute(cpu, opcode)

        // Then - memory should be overwritten with BCD of 15
        assertThat(cpu.memory.readByte(memoryAddress.toInt())).isEqualTo(0.toUByte()) // hundreds
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 1)).isEqualTo(1.toUByte()) // tens
        assertThat(cpu.memory.readByte(memoryAddress.toInt() + 2)).isEqualTo(5.toUByte()) // ones
    }

    companion object {
        @JvmStatic
        fun bcdTestCases(): Stream<Arguments> {
            return Stream.of(
                // value, hundreds, tens, ones
                Arguments.of(0, 0, 0, 0),
                Arguments.of(1, 0, 0, 1),
                Arguments.of(9, 0, 0, 9),
                Arguments.of(10, 0, 1, 0),
                Arguments.of(19, 0, 1, 9),
                Arguments.of(42, 0, 4, 2),
                Arguments.of(99, 0, 9, 9),
                Arguments.of(100, 1, 0, 0),
                Arguments.of(123, 1, 2, 3),
                Arguments.of(200, 2, 0, 0),
                Arguments.of(255, 2, 5, 5) // Maximum value for UByte
            )
        }
    }
}