package com.tomassirio.emulation.chip8.system.cpu.exception

class CommandNotFoundException(opcode: UShort) :
    IllegalArgumentException("Unknown opcode: ${opcode.toString(16)}")