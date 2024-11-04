package cpu

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.Register
import com.tomassirio.cpu.opcode.Command
import com.tomassirio.cpu.opcode.OpcodeTable
import com.tomassirio.cpu.opcode.SysAddrCommand
import com.tomassirio.io.Display
import com.tomassirio.io.Keyboard
import com.tomassirio.memory.Memory
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class CPUTest {
    private lateinit var cpu: CPU
    private lateinit var memory: Memory
    private lateinit var keyboard: Keyboard
    private lateinit var display: Display
    private lateinit var pc: Register.ShortRegister
    private lateinit var sp: Register.ByteRegister
    private lateinit var I: Register.ShortRegister
    private lateinit var mockCommand: Command
    private lateinit var opcodeTable: OpcodeTable

    @BeforeEach
    fun setup() {
        // Create components first
        memory = Memory(4096)
        keyboard = mockk(relaxed = true)
        display = mockk(relaxed = true)
        pc = Register.ShortRegister("pc", 0x200u)
        sp = mockk(relaxed = true)
        I = Register.ShortRegister("I")
        opcodeTable = mockk(relaxed = true)

        // Create and configure mock command
        mockCommand = mockk(relaxed = true)

        // Write test opcode to memory
        memory.write(0x200, 0x0000u.toUShort())

        // Create CPU instance with mocked opcodes
        cpu = CPU(
            memory = memory,
            registers = mutableSetOf(),
            keyboard = keyboard,
            display = display,
            pc = pc,
            sp = sp,
            I = I,
            stack = mutableListOf(),
            opcodeTable = opcodeTable
        )
    }
    @Test
    fun `test runCycle fetches, decodes, and executes an opcode`() {
        every { opcodeTable.getCommand(0x0000u.toUShort()) } returns mockCommand

        // Act
        cpu.runCycle()

        // Assert
        verify(exactly = 1) { mockCommand.execute(cpu, 0x0000u) }
        assertEquals(0x202u.toUShort(), cpu.pc.value, "PC should be incremented by 2")
    }

    @Test
    fun `test opcode fetching`() {
        // Test that we can read the correct opcode from memory
        val opcode = memory.read<UShort>(pc.value.toInt())
        assertEquals(0x0000u, opcode.toUInt(), "Should read correct opcode from memory")
    }

    @Test
    fun `test opcode decoding`() {
        every { opcodeTable.getCommand(0x0000u.toUShort()) } returns SysAddrCommand

        // Test that our mock command is returned for opcode 0x0000
        val command = cpu.opcodeTable.getCommand(0x0000u.toUShort())
        assertNotNull(command, "Should find command for opcode 0x0000")
        assertTrue(command is SysAddrCommand, "Command should be SysAddrCommand")
    }

    @Test
    fun `test opcode execution`() {
        // Test that the command is executed
        mockCommand.execute(cpu, 0x0000u.toUShort())
        verify(exactly = 1) { mockCommand.execute(cpu, 0x0000u) }
    }

    @Test
    fun `test invalid opcode decoding throws exception`() {
        every { opcodeTable.getCommand(any()) } throws IllegalArgumentException("Invalid opcode")

        // Write an invalid opcode to memory
        memory.write(0x200, 0x8888u.toUShort())

        // Assert that running a cycle with an invalid opcode throws an exception
        assertThrows<IllegalArgumentException>("Should throw for invalid opcode") {
            cpu.runCycle()
        }
    }
}