package com.tomassirio.emulation.chip8.system.cpu.opcode

import com.tomassirio.emulation.chip8.system.cpu.exception.CommandNotFoundException
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.addIVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.addVxByteCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.addVxVyCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.andVxVyCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.callAddrCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.clsCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.drwVxVyNCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.exitCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.highCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.jpAddrCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.jpAddrV0Command
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldBVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldDTVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldFVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldIToAddrCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldIVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldRVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldSTVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldVxByteCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldVxDTCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldVxICommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldVxKCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldVxRCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldVxVyCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.ldhfVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.lowCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.orVxVyCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.retCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.rndVxByteCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.scdNCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.sclCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.scrCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.seVxByteCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.seVxVyCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.shlVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.shrVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.sknpVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.skpVxCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.sneVxByteCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.sneVxVyCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.subVxVyCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.subnVxVyCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.sysAddrCommand
import com.tomassirio.emulation.chip8.system.cpu.opcode.commands.xorVxVyCommand
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