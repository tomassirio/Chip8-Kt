package com.tomassirio.emulation.chip8.system.cpu.utils

import java.util.*

class SizedStack<T>(val maxSize: Int) : Stack<T>() {
    override fun push(item: T): T {
        if (size >= maxSize) {
            throw StackOverflowError("Stack has reached maximum size of $maxSize")
        }
        return super.push(item)
    }
}