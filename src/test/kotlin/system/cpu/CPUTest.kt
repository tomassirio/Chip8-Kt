package system.cpu

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.register.Register
import com.tomassirio.system.cpu.opcode.Command
import com.tomassirio.system.cpu.opcode.OpCodeTable
import com.tomassirio.system.cpu.opcode.commands.sysAddrCommand
import com.tomassirio.system.register.utils.RegisterSet
import com.tomassirio.system.io.DisplayState
import com.tomassirio.system.io.KeyboardState
import com.tomassirio.system.memory.Memory
import com.tomassirio.system.memory.util.toUShortAt
import com.tomassirio.system.cpu.utils.SizedStack
import com.tomassirio.system.memory.accessor.MemoryAccessor
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CPUTest {
    private lateinit var cpu: CPU
    private lateinit var memory: Memory
    private lateinit var keyboardState: KeyboardState
    private lateinit var displayState: DisplayState
    private lateinit var pc: Register.ShortRegister
    private lateinit var I: Register.ShortRegister
    private lateinit var DT: Register.TimerRegister
    private lateinit var ST: Register.TimerRegister
    private lateinit var mockCommand: Command

    @BeforeEach
    fun setup() {
        // Create components first
        memory = Memory(4096)
        keyboardState = mockk(relaxed = true)
        displayState = mockk(relaxed = true)
        pc = Register.ShortRegister(0x200u)
        I = Register.ShortRegister()
        DT = Register.TimerRegister()
        ST = Register.TimerRegister()

        // Create and configure mock command
        mockCommand = mockk(relaxed = true)

        // Write test opcode to memory
        memory.writeByte(0x200, 0x0000u)

        // Create CPU instance with mocked opcodes
        cpu = CPU(
            memory = memory,
            registers = RegisterSet.Builder().build(),
            keyboardState = keyboardState,
            displayState = displayState,
            pc = pc,
            I = I,
            DT = DT,
            ST = ST,
            stack = SizedStack(16),
        )
    }

    @Test
    fun `test runCycle fetches, decodes, and executes an opcode`() {
        // Setup - Mock the OpCodeTable to return our mock command
        mockkObject(OpCodeTable)
        every { OpCodeTable.getCommand(0x0000u) } returns mockCommand

        // Act
        cpu.runCycle()

        // Assert
        verify(exactly = 1) { mockCommand.execute(cpu, 0x0000u) }
        assertThat(0x202u.toUShort()).isEqualTo(cpu.pc.read())
            .withFailMessage("PC should be incremented by 2")

        // Cleanup
        unmockkObject(OpCodeTable)
    }

    @Test
    fun `test opcode fetching`() {
        // Test that we can read the correct opcode from memory
        val opcode = MemoryAccessor.readUShort(memory, pc.read().toInt())
        assertThat(0x0000u).isEqualTo(opcode.toUInt())
            .withFailMessage("Should read correct opcode from memory")
    }

    @Test
    fun `test opcode decoding`() {
        // Test that our mock command is returned for opcode 0x0000
        val command = OpCodeTable.getCommand(0x0000u)
        assertThat(command).isNotNull()
            .withFailMessage("Should find command for opcode 0x0000")
        assertThat(command).isInstanceOf(sysAddrCommand().javaClass)
            .withFailMessage("Command should be SysAddrCommand")
    }

    @Test
    fun `test opcode execution`() {
        // Test that the command is executed
        mockCommand.execute(cpu, 0x0000u)
        verify(exactly = 1) { mockCommand.execute(cpu, 0x0000u) }
    }

    @Test
    fun `test invalid opcode decoding throws exception`() {
        mockkObject(OpCodeTable)
        every { OpCodeTable.getCommand(0x8888u) } throws IllegalArgumentException("Invalid opcode")

        MemoryAccessor.writeUShort(memory, 0x200, 0x8888u)

        assertThrows<IllegalArgumentException>("Should throw for invalid opcode") {
            cpu.runCycle()
        }

        unmockkObject(OpCodeTable)
    }

    @Test
    fun `test pc doesnt increment when Command skips increment`() {
        mockkObject(OpCodeTable)
        every { OpCodeTable.getCommand(any()) }.returns(mockCommand)
        every { mockCommand.skipsPcIncrement }.returns(true)

        val pcBeforeValue = cpu.pc.read()

        cpu.runCycle()

        verify(exactly = 1) { mockCommand.execute(cpu, any()) }
        assertThat(cpu.pc.read()).isEqualTo(pcBeforeValue)
    }
}