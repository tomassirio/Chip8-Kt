package io.github.tomassirio.system.cpu.exception

class CommandNotFoundException(opcode: UShort) :
    IllegalArgumentException("Unknown opcode: ${opcode.toString(16)}")