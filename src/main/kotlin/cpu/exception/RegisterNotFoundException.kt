package com.tomassirio.cpu.exception


class RegisterNotFoundException(registerIndex: Short) : IllegalArgumentException("Register '$registerIndex' not found")