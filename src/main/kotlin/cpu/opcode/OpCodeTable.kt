package com.tomassirio.cpu.opcode

import com.tomassirio.cpu.exception.CommandNotFoundException
import com.tomassirio.cpu.opcode.commands.CALLAddrCommand
import com.tomassirio.cpu.opcode.commands.CLSCommand
import com.tomassirio.cpu.opcode.commands.JPAddrCommand
import com.tomassirio.cpu.opcode.commands.RETCommand
import com.tomassirio.cpu.opcode.commands.SYSAddrCommand

object OpCodeTable {
    val chip8CommandGetter: (UShort) -> Command = ::getCommand
    val chip48CommandGetter: (UShort) -> Command = ::getChip48Command

    private fun getCommand(opcode: UShort): Command {
        return when (opcode.and(0xFFFu).toUInt()) {
            0x0000u -> SYSAddrCommand
            0x00E0u -> CLSCommand
            0x00EEu -> RETCommand
            0x1000u -> JPAddrCommand
            0x2000u -> CALLAddrCommand
//            0x3000u -> SEVxByteCommand
//            0x4000u -> SNEVxByteCommand
//            0x5000u -> SEVxVyCommand
//            0x6000u -> LDVxByteCommand
//            0x7000u -> ADDVxByteCommand
//            0x8000u -> when (opcode.and(0xFu).toUInt()) {
//                0x0u -> LDVxVyCommand
//                0x1u -> ORVxVyCommand
//                0x2u -> ANDVxVyCommand
//                0x3u -> XORVxVyCommand
//                0x4u -> ADDVxVyCommand
//                0x5u -> SUBVxVyCommand
//                0x6u -> SHRCommand
//                0x7u -> SUBNVxVyCommand
//                0xEu -> SHLCommand
//                else -> throw CommandNotFoundException(opcode)
//            }
//            0x9000u -> SNEVxVyCommand
//            0xA000u -> LDIToAddrCommand
//            0xB000u -> JPAddrV0Command
//            0xC000u -> RNDVxByteCommand
//            0xD000u -> DRWVxVyNCommand
//            0xE000u -> when (opcode.and(0xFFu).toUInt()) {
//                0x9Eu -> SKPVxCommand
//                0xA1u -> SKNPVxCommand
//                else -> throw CommandNotFoundException(opcode)
//            }
//            0xF000u -> when (opcode.and(0xFFu).toUInt()) {
//                0x07u -> LDVxDTCommand
//                0x0Au -> LDVxKCommand
//                0x15u -> LDDTVxCommand
//                0x18u -> LDSTVxCommand
//                0x1Eu -> ADDIVxCommand
//                0x29u -> LDFVxCommand
//                0x33u -> LDBVxCommand
//                0x55u -> LDIVxCommand
//                0x65u -> LDVxICommand
//                else -> throw CommandNotFoundException(opcode)
//            }
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