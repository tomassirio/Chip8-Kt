package com.tomassirio.system.cpu.utils

import java.util.Stack


class SizedStack<T>(val maxSize: Int) : Stack<T>() {
    override fun push(item: T): T {
        while (size >= maxSize) {
            removeAt(0)
        }
        return super.push(item)
    }
}