package cpu.opcode

import com.tomassirio.cpu.exception.CommandNotFoundException
import com.tomassirio.cpu.opcode.commands.CLSCommand
import com.tomassirio.cpu.opcode.Command
import com.tomassirio.cpu.opcode.OpCodeTable
import com.tomassirio.cpu.opcode.commands.ADDVxByteCommand
import com.tomassirio.cpu.opcode.commands.ADDVxVyCommand
import com.tomassirio.cpu.opcode.commands.ANDVxVyCommand
import com.tomassirio.cpu.opcode.commands.CALLAddrCommand
import com.tomassirio.cpu.opcode.commands.DRWVxVyNCommand
import com.tomassirio.cpu.opcode.commands.JPAddrCommand
import com.tomassirio.cpu.opcode.commands.JPAddrV0Command
import com.tomassirio.cpu.opcode.commands.LDIToAddrCommand
import com.tomassirio.cpu.opcode.commands.LDVxByteCommand
import com.tomassirio.cpu.opcode.commands.LDVxVyCommand
import com.tomassirio.cpu.opcode.commands.ORVxVyCommand
import com.tomassirio.cpu.opcode.commands.RETCommand
import com.tomassirio.cpu.opcode.commands.RNDVxByteCommand
import com.tomassirio.cpu.opcode.commands.SEVxByteCommand
import com.tomassirio.cpu.opcode.commands.SEVxVyCommand
import com.tomassirio.cpu.opcode.commands.SHLVxCommand
import com.tomassirio.cpu.opcode.commands.SHRVxCommand
import com.tomassirio.cpu.opcode.commands.SKPVxCommand
import com.tomassirio.cpu.opcode.commands.SNEVxByteCommand
import com.tomassirio.cpu.opcode.commands.SNEVxVyCommand
import com.tomassirio.cpu.opcode.commands.SUBNVxVyCommand
import com.tomassirio.cpu.opcode.commands.SUBVxVyCommand
import com.tomassirio.cpu.opcode.commands.SYSAddrCommand
import com.tomassirio.cpu.opcode.commands.XORVxVyCommand
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random

class OpcodeTableTest {
    @ParameterizedTest
    @MethodSource("opcodeChip8Provider")
    fun `test getCommand Chip8 returns the correct command`(
        opcode: Int,
        expectedCommand: Command) {
        val opcodeTable = OpCodeTable.chip8CommandGetter
        val command = opcodeTable(opcode.toUShort())
        assertThat(expectedCommand).isEqualTo(command)
    }

    @Test
    fun `test getCommand Chip8 throws CommandNotFoundException for unknown opcode`() {
        val opcodeTable = OpCodeTable.chip8CommandGetter
        val unknownOpcode = 0xFFFF
        assertThatThrownBy { opcodeTable(unknownOpcode.toUShort())}
            .isInstanceOf(CommandNotFoundException::class.java)
    }

    companion object {
        @JvmStatic
        fun opcodeChip8Provider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0x0000, SYSAddrCommand),
                Arguments.of(0x00E0, CLSCommand),
                Arguments.of(0x00EE, RETCommand),
                Arguments.of(0x1000, JPAddrCommand),
                Arguments.of(0x2000, CALLAddrCommand),
                Arguments.of(0x3000, SEVxByteCommand),
                Arguments.of(0x4000, SNEVxByteCommand),
                Arguments.of(0x5000, SEVxVyCommand),
                Arguments.of(0x6000, LDVxByteCommand),
                Arguments.of(0x7000, ADDVxByteCommand),
                Arguments.of(0x8000, LDVxVyCommand),
                Arguments.of(0x8001, ORVxVyCommand),
                Arguments.of(0x8002, ANDVxVyCommand),
                Arguments.of(0x8003, XORVxVyCommand),
                Arguments.of(0x8004, ADDVxVyCommand),
                Arguments.of(0x8005, SUBVxVyCommand),
                Arguments.of(0x8006, SHRVxCommand),
                Arguments.of(0x8007, SUBNVxVyCommand),
                Arguments.of(0x800E, SHLVxCommand),
                Arguments.of(0x9000, SNEVxVyCommand),
                Arguments.of(0xA000, LDIToAddrCommand),
                Arguments.of(0xB000, JPAddrV0Command),
                Arguments.of(0xC000, RNDVxByteCommand(Random.Default)),
                Arguments.of(0xD000, DRWVxVyNCommand),
                Arguments.of(0xE09E, SKPVxCommand)
            )
        }
    }
}