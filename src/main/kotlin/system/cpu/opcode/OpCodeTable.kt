package com.tomassirio.system.cpu.opcode

import com.tomassirio.system.cpu.exception.CommandNotFoundException
import com.tomassirio.system.cpu.opcode.commands.SUBNVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.SUBVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.ADDVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.ADDVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.ANDVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.CALLAddrCommand
import com.tomassirio.system.cpu.opcode.commands.CLSCommand
import com.tomassirio.system.cpu.opcode.commands.DRWVxVyNCommand
import com.tomassirio.system.cpu.opcode.commands.JPAddrCommand
import com.tomassirio.system.cpu.opcode.commands.JPAddrV0Command
import com.tomassirio.system.cpu.opcode.commands.LDDTVxCommand
import com.tomassirio.system.cpu.opcode.commands.LDIToAddrCommand
import com.tomassirio.system.cpu.opcode.commands.LDSTVxCommand
import com.tomassirio.system.cpu.opcode.commands.LDVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.LDVxDTCommand
import com.tomassirio.system.cpu.opcode.commands.LDVxKCommand
import com.tomassirio.system.cpu.opcode.commands.LDVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.ORVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.RETCommand
import com.tomassirio.system.cpu.opcode.commands.RNDVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.SEVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.SEVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.SHLVxCommand
import com.tomassirio.system.cpu.opcode.commands.SHRVxCommand
import com.tomassirio.system.cpu.opcode.commands.SKNPVxCommand
import com.tomassirio.system.cpu.opcode.commands.SKPVxCommand
import com.tomassirio.system.cpu.opcode.commands.SNEVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.SNEVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.SYSAddrCommand
import com.tomassirio.system.cpu.opcode.commands.XORVxVyCommand
import kotlin.random.Random

object OpCodeTable {
    val chip8CommandGetter: (UShort) -> Command = OpCodeTable::getCommand
    val chip48CommandGetter: (UShort) -> Command = OpCodeTable::getChip48Command

    fun getCommand(opcode: UShort): Command {
        return when (opcode.and(0xF000u).toUInt()) {
            0x0000u -> when (opcode.and(0xFFu).toUInt()) {
                0x0000u -> SYSAddrCommand
                0x00E0u -> CLSCommand
                0x00EEu -> RETCommand
                else -> throw CommandNotFoundException(opcode)
            }
            0x1000u -> JPAddrCommand
            0x2000u -> CALLAddrCommand
            0x3000u -> SEVxByteCommand
            0x4000u -> SNEVxByteCommand
            0x5000u -> SEVxVyCommand
            0x6000u -> LDVxByteCommand
            0x7000u -> ADDVxByteCommand
            0x8000u -> when (opcode.and(0xFu).toUInt()) {
                0x0u -> LDVxVyCommand
                0x1u -> ORVxVyCommand
                0x2u -> ANDVxVyCommand
                0x3u -> XORVxVyCommand
                0x4u -> ADDVxVyCommand
                0x5u -> SUBVxVyCommand
                0x6u -> SHRVxCommand
                0x7u -> SUBNVxVyCommand
                0xEu -> SHLVxCommand
                else -> throw CommandNotFoundException(opcode)
            }
            0x9000u -> SNEVxVyCommand
            0xA000u -> LDIToAddrCommand
            0xB000u -> JPAddrV0Command
            0xC000u -> RNDVxByteCommand(Random.Default)
            0xD000u -> DRWVxVyNCommand
            0xE000u -> when (opcode.and(0xFFu).toUInt()) {
                0x9Eu -> SKPVxCommand
                0xA1u -> SKNPVxCommand
                else -> throw CommandNotFoundException(opcode)
            }
            0xF000u -> when (opcode.and(0xFFu).toUInt()) {
                0x07u -> LDVxDTCommand
                0x0Au -> LDVxKCommand
                0x15u -> LDDTVxCommand
                0x18u -> LDSTVxCommand
//                0x1Eu -> ADDIVxCommand
//                0x29u -> LDFVxCommand
//                0x33u -> LDBVxCommand
//                0x55u -> LDIVxCommand
//                0x65u -> LDVxICommand
                else -> throw CommandNotFoundException(opcode)
            }
            else -> throw CommandNotFoundException(opcode)
        }
    }

    private fun getChip48Command(opcode: UShort): Command {
        return when(opcode.and(0xFFFu).toUInt()) {
            0x0000u -> when (opcode.and(0xFFu).toUInt()) {
//                0xC0u -> SCDCommand //fixme
//                0x00FBu -> SCRCommand
//                0x00FCu -> SCLCommand
//                0x00FDu -> EXITCommand
//                0x00FEu -> LOWCommand
//                0x00FFu -> HIGHCommand
                else -> getCommand(opcode)
            }
//            0xD000u -> DRWVxVyNCommand
            0xF000u -> when (opcode.and(0xFFu).toUInt()) {
//                0x30u -> LDHFVxCommand
//                0x75u -> LDRVxCommand
//                0x85u -> LDRVxCommand
                else -> getCommand(opcode)
            }
            else -> getCommand(opcode)
        }
    }
}