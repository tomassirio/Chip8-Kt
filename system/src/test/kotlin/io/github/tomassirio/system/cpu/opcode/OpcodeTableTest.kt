package io.github.tomassirio.system.cpu.opcode

import io.github.tomassirio.system.cpu.exception.CommandNotFoundException
import io.github.tomassirio.system.cpu.opcode.commands.addIVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.addVxByteCommand
import io.github.tomassirio.system.cpu.opcode.commands.addVxVyCommand
import io.github.tomassirio.system.cpu.opcode.commands.andVxVyCommand
import io.github.tomassirio.system.cpu.opcode.commands.callAddrCommand
import io.github.tomassirio.system.cpu.opcode.commands.clsCommand
import io.github.tomassirio.system.cpu.opcode.commands.drwVxVyNCommand
import io.github.tomassirio.system.cpu.opcode.commands.exitCommand
import io.github.tomassirio.system.cpu.opcode.commands.highCommand
import io.github.tomassirio.system.cpu.opcode.commands.jpAddrCommand
import io.github.tomassirio.system.cpu.opcode.commands.jpAddrV0Command
import io.github.tomassirio.system.cpu.opcode.commands.ldBVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldDTVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldFVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldIToAddrCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldIVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldRVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldSTVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldVxByteCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldVxDTCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldVxICommand
import io.github.tomassirio.system.cpu.opcode.commands.ldVxKCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldVxRCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldVxVyCommand
import io.github.tomassirio.system.cpu.opcode.commands.ldhfVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.lowCommand
import io.github.tomassirio.system.cpu.opcode.commands.orVxVyCommand
import io.github.tomassirio.system.cpu.opcode.commands.retCommand
import io.github.tomassirio.system.cpu.opcode.commands.rndVxByteCommand
import io.github.tomassirio.system.cpu.opcode.commands.scdNCommand
import io.github.tomassirio.system.cpu.opcode.commands.sclCommand
import io.github.tomassirio.system.cpu.opcode.commands.scrCommand
import io.github.tomassirio.system.cpu.opcode.commands.seVxByteCommand
import io.github.tomassirio.system.cpu.opcode.commands.seVxVyCommand
import io.github.tomassirio.system.cpu.opcode.commands.shlVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.shrVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.sknpVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.skpVxCommand
import io.github.tomassirio.system.cpu.opcode.commands.sneVxByteCommand
import io.github.tomassirio.system.cpu.opcode.commands.sneVxVyCommand
import io.github.tomassirio.system.cpu.opcode.commands.subVxVyCommand
import io.github.tomassirio.system.cpu.opcode.commands.subnVxVyCommand
import io.github.tomassirio.system.cpu.opcode.commands.sysAddrCommand
import io.github.tomassirio.system.cpu.opcode.commands.xorVxVyCommand
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
        expectedCommand: Command
    ) {
        val opcodeTable = OpCodeTable.chip8CommandGetter
        val command = opcodeTable(opcode.toUShort())
        assertThat(expectedCommand).usingRecursiveComparison().isEqualTo(command)
    }

    @ParameterizedTest
    @MethodSource("opcodeChip48Provider")
    fun `test getCommand Chip48 returns the correct command`(
        opcode: Int,
        expectedCommand: Command
    ) {
        val opcodeTable = OpCodeTable.chip48CommandGetter
        val command = opcodeTable(opcode.toUShort())
        assertThat(expectedCommand).usingRecursiveComparison().isEqualTo(command)
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
                Arguments.of(0xF055, ldIVxCommand()),
                Arguments.of(0xF065, ldVxICommand()),
            )
        }

        @JvmStatic
        fun opcodeChip48Provider(): Stream<Arguments> {
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
                Arguments.of(0xF055, ldIVxCommand()),
                Arguments.of(0xF065, ldVxICommand()),

                //Chip48 methods
                Arguments.of(0x00C0, scdNCommand()),
                Arguments.of(0x00C1, scdNCommand()),
                Arguments.of(0x00C2, scdNCommand()),
                Arguments.of(0x00C3, scdNCommand()),
                Arguments.of(0x00C4, scdNCommand()),
                Arguments.of(0x00C5, scdNCommand()),
                Arguments.of(0x00C6, scdNCommand()),
                Arguments.of(0x00C7, scdNCommand()),
                Arguments.of(0x00C8, scdNCommand()),
                Arguments.of(0x00C9, scdNCommand()),
                Arguments.of(0x00CA, scdNCommand()),
                Arguments.of(0x00CB, scdNCommand()),
                Arguments.of(0x00CC, scdNCommand()),
                Arguments.of(0x00CD, scdNCommand()),
                Arguments.of(0x00CE, scdNCommand()),
                Arguments.of(0x00CF, scdNCommand()),
                Arguments.of(0x00FB, scrCommand()),
                Arguments.of(0x00FC, sclCommand()),
                Arguments.of(0x00FD, exitCommand()),
                Arguments.of(0x00FE, lowCommand()),
                Arguments.of(0x00FF, highCommand()),
                Arguments.of(0xF030, ldhfVxCommand()),
                Arguments.of(0xF075, ldRVxCommand()),
                Arguments.of(0xF085, ldVxRCommand()),
            )
        }
    }
}