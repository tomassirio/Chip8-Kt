package system.cpu.opcode

import com.tomassirio.system.cpu.exception.CommandNotFoundException
import com.tomassirio.system.cpu.opcode.Command
import com.tomassirio.system.cpu.opcode.OpCodeTable
import com.tomassirio.system.cpu.opcode.commands.addIVxCommand
import com.tomassirio.system.cpu.opcode.commands.addVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.addVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.andVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.callAddrCommand
import com.tomassirio.system.cpu.opcode.commands.clsCommand
import com.tomassirio.system.cpu.opcode.commands.drwVxVyNCommand
import com.tomassirio.system.cpu.opcode.commands.jpAddrCommand
import com.tomassirio.system.cpu.opcode.commands.jpAddrV0Command
import com.tomassirio.system.cpu.opcode.commands.ldBVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldDTVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldFVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldIToAddrCommand
import com.tomassirio.system.cpu.opcode.commands.ldSTVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.ldVxDTCommand
import com.tomassirio.system.cpu.opcode.commands.ldVxKCommand
import com.tomassirio.system.cpu.opcode.commands.ldVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.orVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.retCommand
import com.tomassirio.system.cpu.opcode.commands.rndVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.seVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.seVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.shlVxCommand
import com.tomassirio.system.cpu.opcode.commands.shrVxCommand
import com.tomassirio.system.cpu.opcode.commands.sknpVxCommand
import com.tomassirio.system.cpu.opcode.commands.skpVxCommand
import com.tomassirio.system.cpu.opcode.commands.sneVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.sneVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.subVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.subnVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.sysAddrCommand
import com.tomassirio.system.cpu.opcode.commands.xorVxVyCommand
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

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
                Arguments.of(0x0000, sysAddrCommand()),
                Arguments.of(0x00E0, clsCommand()),
                Arguments.of(0x00EE, retCommand()),
                Arguments.of(0x1000, jpAddrCommand()),
                Arguments.of(0x2000, callAddrCommand()),
                Arguments.of(0x3000, seVxByteCommand()),
                Arguments.of(0x4000, sneVxByteCommand()),
                Arguments.of(0x5000, seVxVyCommand()),
                Arguments.of(0x6000, ldVxByteCommand()),
                Arguments.of(0x7000, addVxByteCommand()),
                Arguments.of(0x8000, ldVxVyCommand()),
                Arguments.of(0x8001, orVxVyCommand()),
                Arguments.of(0x8002, andVxVyCommand()),
                Arguments.of(0x8003, xorVxVyCommand()),
                Arguments.of(0x8004, addVxVyCommand()),
                Arguments.of(0x8005, subVxVyCommand()),
                Arguments.of(0x8006, shrVxCommand()),
                Arguments.of(0x8007, subnVxVyCommand()),
                Arguments.of(0x800E, shlVxCommand()),
                Arguments.of(0x9000, sneVxVyCommand()),
                Arguments.of(0xA000, ldIToAddrCommand()),
                Arguments.of(0xB000, jpAddrV0Command()),
                Arguments.of(0xC000, rndVxByteCommand()),
                Arguments.of(0xD000, drwVxVyNCommand()),
                Arguments.of(0xE09E, skpVxCommand()),
                Arguments.of(0xE0A1, sknpVxCommand()),
                Arguments.of(0xF007, ldVxDTCommand()),
                Arguments.of(0xF00A, ldVxKCommand()),
                Arguments.of(0xF015, ldDTVxCommand()),
                Arguments.of(0xF018, ldSTVxCommand()),
                Arguments.of(0xF01E, addIVxCommand()),
                Arguments.of(0xF029, ldFVxCommand()),
                Arguments.of(0xF033, ldBVxCommand()),
            )
        }
    }
}