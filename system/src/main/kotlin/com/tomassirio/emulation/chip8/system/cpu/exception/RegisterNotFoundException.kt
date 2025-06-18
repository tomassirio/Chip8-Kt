package com.tomassirio.emulation.chip8.system.cpu.exception

class RegisterNotFoundException(registerIndex: Short) : IllegalArgumentException("Register '$registerIndex' not found")