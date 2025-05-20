package com.tomassirio.system.cpu.exception

class CommandNotFoundException(opcode: UShort) :
    IllegalArgumentException("Unknown opcode: ${opcode.toString(16)}")