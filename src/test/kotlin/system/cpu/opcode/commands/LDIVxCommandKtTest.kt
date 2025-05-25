package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.ldIVxCommand
import com.tomassirio.system.memory.util.toUByteAt
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalUnsignedTypes::class)
class LDIVxCommandKtTest {
    private val ldIVxCommand = ldIVxCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `test ldIVxCommand stores single register V0 to memory`() {
        // Given
        val memoryAddress = 0x300.toUShort()
        val opcode = 0xF055.toUShort() // F055 - LD [I], V0

        cpu.I.write(memoryAddress)
        cpu.registers[0].write(0x42.toUByte())

        // When
        ldIVxCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.memory.read(memoryAddress.toInt()){ bytes, addr ->
            bytes.toUByteAt(addr)
        }).isEqualTo(0x42.toUByte())
    }

    @Test
    fun `test ldIVxCommand stores multiple registers V0 through V3 to memory`() {
        // Given
        val memoryAddress = 0x400.toUShort()
        val opcode = 0xF355.toUShort() // F355 - LD [I], V3

        cpu.I.write(memoryAddress)
        cpu.registers[0].write(0x10.toUByte())
        cpu.registers[1].write(0x20.toUByte())
        cpu.registers[2].write(0x30.toUByte())
        cpu.registers[3].write(0x40.toUByte())
        cpu.registers[4].write(0x50.toUByte()) // Should not be stored

        // When
        ldIVxCommand.execute(cpu, opcode)

        // Then
        assertThat(cpu.memory.read(memoryAddress.toInt()){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0x10.toUByte())
        assertThat(cpu.memory.read(memoryAddress.toInt() + 1){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0x20.toUByte())
        assertThat(cpu.memory.read(memoryAddress.toInt() + 2){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0x30.toUByte())
        assertThat(cpu.memory.read(memoryAddress.toInt() + 3){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0x40.toUByte())
        // V4 should not be stored, memory should remain 0
        assertThat(cpu.memory.read(memoryAddress.toInt() + 4){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0x00.toUByte())
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 5, 10, 15]) // Test different register counts
    fun `test ldIVxCommand stores correct number of registers`(registerIndex: Int) {
        // Given
        val memoryAddress = 0x500.toUShort()
        val opcode = ((0xF055) or (registerIndex shl 8)).toUShort() // Fx55

        cpu.I.write(memoryAddress)

        // Set values in all registers
        for (i in 0..15) {
            cpu.registers[i].write((0x10 + i).toUByte())
        }

        // When
        ldIVxCommand.execute(cpu, opcode)

        // Then - Check that V0 through Vx are stored
        for (i in 0..registerIndex) {
            assertThat(cpu.memory.read(memoryAddress.toInt() + i){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo((0x10 + i).toUByte())
        }

        // Check that Vx+1 is not stored (if within bounds)
        if (registerIndex < 15) {
            assertThat(cpu.memory.read(memoryAddress.toInt() + registerIndex + 1){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0x00.toUByte())
        }
    }

    @Test
    fun `test ldIVxCommand uses I register for memory address`() {
        // Given
        val memoryAddress1 = 0x200.toUShort()
        val memoryAddress2 = 0x300.toUShort()
        val opcode = 0xF155.toUShort() // F155 - LD [I], V1

        cpu.registers[0].write(0xAA.toUByte())
        cpu.registers[1].write(0xBB.toUByte())

        // Test with first address
        cpu.I.write(memoryAddress1)
        ldIVxCommand.execute(cpu, opcode)

        assertThat(cpu.memory.read(memoryAddress1.toInt()){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0xAA.toUByte())
        assertThat(cpu.memory.read(memoryAddress1.toInt() + 1){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0xBB.toUByte())

        // Test with second address
        cpu.I.write(memoryAddress2)
        ldIVxCommand.execute(cpu, opcode)

        assertThat(cpu.memory.read(memoryAddress2.toInt()){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0xAA.toUByte())
        assertThat(cpu.memory.read(memoryAddress2.toInt() + 1){ bytes, addr ->
                bytes.toUByteAt(addr)
            }).isEqualTo(0xBB.toUByte())
    }
}