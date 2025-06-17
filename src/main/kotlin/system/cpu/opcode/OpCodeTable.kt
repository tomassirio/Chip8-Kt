package com.tomassirio.system.cpu.opcode

import com.tomassirio.system.cpu.exception.CommandNotFoundException

import com.tomassirio.system.cpu.opcode.commands.addIVxCommand
import com.tomassirio.system.cpu.opcode.commands.addVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.addVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.andVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.callAddrCommand
import com.tomassirio.system.cpu.opcode.commands.clsCommand
import com.tomassirio.system.cpu.opcode.commands.drwVxVyNCommand
import com.tomassirio.system.cpu.opcode.commands.exitCommand
import com.tomassirio.system.cpu.opcode.commands.highCommand
import com.tomassirio.system.cpu.opcode.commands.jpAddrCommand
import com.tomassirio.system.cpu.opcode.commands.jpAddrV0Command
import com.tomassirio.system.cpu.opcode.commands.ldBVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldDTVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldFVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldIToAddrCommand
import com.tomassirio.system.cpu.opcode.commands.ldIVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldRVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldSTVxCommand
import com.tomassirio.system.cpu.opcode.commands.ldVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.ldVxDTCommand
import com.tomassirio.system.cpu.opcode.commands.ldVxICommand
import com.tomassirio.system.cpu.opcode.commands.ldVxKCommand
import com.tomassirio.system.cpu.opcode.commands.ldVxRCommand
import com.tomassirio.system.cpu.opcode.commands.ldVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.ldhfVxCommand
import com.tomassirio.system.cpu.opcode.commands.lowCommand
import com.tomassirio.system.cpu.opcode.commands.orVxVyCommand
import com.tomassirio.system.cpu.opcode.commands.retCommand
import com.tomassirio.system.cpu.opcode.commands.rndVxByteCommand
import com.tomassirio.system.cpu.opcode.commands.scdNCommand
import com.tomassirio.system.cpu.opcode.commands.sclCommand
import com.tomassirio.system.cpu.opcode.commands.scrCommand
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

object OpCodeTable {
    val chip8CommandGetter: (UShort) -> Command = OpCodeTable::getCommand
    val chip48CommandGetter: (UShort) -> Command = OpCodeTable::getChip48Command

    fun getCommand(opcode: UShort): Command {
        return when (opcode.and(0xF000u).toUInt()) {
            0x0000u -> when (opcode.and(0xFFu).toUInt()) {
                0x0000u -> sysAddrCommand()
                0x00E0u -> clsCommand()
                0x00EEu -> retCommand()
                else -> throw CommandNotFoundException(opcode)
            }
            0x1000u -> jpAddrCommand()
            0x2000u -> callAddrCommand()
            0x3000u -> seVxByteCommand()
            0x4000u -> sneVxByteCommand()
            0x5000u -> seVxVyCommand()
            0x6000u -> ldVxByteCommand()
            0x7000u -> addVxByteCommand()
            0x8000u -> when (opcode.and(0xFu).toUInt()) {
                0x0u -> ldVxVyCommand()
                0x1u -> orVxVyCommand()
                0x2u -> andVxVyCommand()
                0x3u -> xorVxVyCommand()
                0x4u -> addVxVyCommand()
                0x5u -> subVxVyCommand()
                0x6u -> shrVxCommand()
                0x7u -> subnVxVyCommand()
                0xEu -> shlVxCommand()
                else -> throw CommandNotFoundException(opcode)
            }
            0x9000u -> sneVxVyCommand()
            0xA000u -> ldIToAddrCommand()
            0xB000u -> jpAddrV0Command()
            0xC000u -> rndVxByteCommand()
            0xD000u -> drwVxVyNCommand()
            0xE000u -> when (opcode.and(0xFFu).toUInt()) {
                0x9Eu -> skpVxCommand()
                0xA1u -> sknpVxCommand()
                else -> throw CommandNotFoundException(opcode)
            }
            0xF000u -> when (opcode.and(0xFFu).toUInt()) {
                0x07u -> ldVxDTCommand()
                0x0Au -> ldVxKCommand()
                0x15u -> ldDTVxCommand()
                0x18u -> ldSTVxCommand()
                0x1Eu -> addIVxCommand()
                0x29u -> ldFVxCommand()
                0x33u -> ldBVxCommand()
                0x55u -> ldIVxCommand()
                0x65u -> ldVxICommand()
                else -> throw CommandNotFoundException(opcode)
            }
            else -> throw CommandNotFoundException(opcode)
        }
    }

    private fun getChip48Command(opcode: UShort): Command {
        return when (opcode.toUInt()) {
            in 0x00C0u..0x00CFu -> scdNCommand()
            0x00FBu -> scrCommand()
            0x00FCu -> sclCommand()
            0x00FDu -> exitCommand()
            0x00FEu -> lowCommand()
            0x00FFu -> highCommand()
            else -> when (opcode.and(0xF000u).toUInt()) {
                0xF000u -> when (opcode.and(0x00FFu).toUInt()) {
                    0x30u -> ldhfVxCommand()
                    0x75u -> ldRVxCommand()
                    0x85u -> ldVxRCommand()
                    else -> getCommand(opcode)
                }
                else -> getCommand(opcode)
            }
        }
    }

}