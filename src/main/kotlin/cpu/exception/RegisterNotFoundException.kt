package com.tomassirio.cpu.exception

class RegisterNotFoundException(registerName: String) : IllegalArgumentException("Register '$registerName' not found")