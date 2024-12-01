package com.tomassirio.cpu.exception

class CommandNotFoundException(opcode: UShort) :
    IllegalArgumentException("Unknown opcode: ${opcode.toString(16)}")