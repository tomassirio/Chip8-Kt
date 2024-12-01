package com.tomassirio.factory

import com.tomassirio.memory.Memory

object MemoryFactory {
    fun createMemory(): Memory {
        return Memory(4096)
    }

    fun createETIMemory(): Memory {
        return Memory(4096, 0x600)
    }
}