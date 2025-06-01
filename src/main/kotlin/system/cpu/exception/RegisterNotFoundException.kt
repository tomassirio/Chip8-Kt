package com.tomassirio.system.cpu.exception


class RegisterNotFoundException(registerIndex: Short) : IllegalArgumentException("Register '$registerIndex' not found")