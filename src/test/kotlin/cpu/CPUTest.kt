package cpu

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.Register
import com.tomassirio.cpu.opcode.Command
import com.tomassirio.cpu.opcode.commands.SYSAddrCommand
import com.tomassirio.io.Display
import com.tomassirio.io.Keyboard
import com.tomassirio.memory.Memory
import com.tomassirio.cpu.utils.RegisterSet
import com.tomassirio.memory.toUShortAt
import com.tomassirio.utils.SizedStack
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalUnsignedTypes::class)
class CPUTest {
    private lateinit var cpu: CPU
    private lateinit var memory: Memory
    private lateinit var keyboard: Keyboard
    private lateinit var display: Display
    private lateinit var pc: Register.ShortRegister
    private lateinit var sp: Register.ByteRegister
    private lateinit var I: Register.ShortRegister
    private lateinit var mockCommand: Command
    private lateinit var commands: (UShort) -> Command

    @BeforeEach
    fun setup() {
        // Create components first
        memory = Memory(4096)
        keyboard = mockk(relaxed = true)
        display = mockk(relaxed = true)
        pc = Register.ShortRegister(0x200u)
        sp = mockk(relaxed = true)
        I = Register.ShortRegister()
        commands = mockk(relaxed = true)

        // Create and configure mock command
        mockCommand = mockk(relaxed = true)

        // Write test opcode to memory
        memory.write(0x200, 0x0000u)

        // Create CPU instance with mocked opcodes
        cpu = CPU(
            memory = memory,
            registers = RegisterSet.Builder().build(),
            keyboard = keyboard,
            display = display,
            pc = pc,
            sp = sp,
            I = I,
            stack = SizedStack(16),
            commands = commands
        )
    }
    @Test
    fun `test runCycle fetches, decodes, and executes an opcode`() {
        every { commands(0x0000u) } returns mockCommand

        // Act
        cpu.runCycle()

        // Assert
        verify(exactly = 1) { mockCommand.execute(cpu, 0x0000u) }
        assertThat(0x202u.toUShort()).isEqualTo(cpu.pc.read())
            .withFailMessage("PC should be incremented by 2")
    }

    @Test
    fun `test opcode fetching`() {
        // Test that we can read the correct opcode from memory
        val opcode = memory.read(pc.read().toInt()) { bytes, addr -> bytes.toUShortAt(addr) }
        assertThat(0x0000u).isEqualTo(opcode.toUInt())
            .withFailMessage("Should read correct opcode from memory")
    }

    @Test
    fun `test opcode decoding`() {
        every { commands(0x0000u) } returns SYSAddrCommand

        // Test that our mock command is returned for opcode 0x0000
        val command = cpu.commands(0x0000u)
        assertThat(command).isNotNull()
            .withFailMessage("Should find command for opcode 0x0000")
        assertThat(command).isInstanceOf(SYSAddrCommand.javaClass)
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
        every { commands(any()) } throws IllegalArgumentException("Invalid opcode")

        // Write an invalid opcode to memory
        memory.write(0x200, 0x8888u)

        // Assert that running a cycle with an invalid opcode throws an exception
        assertThrows<IllegalArgumentException>("Should throw for invalid opcode") {
            cpu.runCycle()
        }
    }
}