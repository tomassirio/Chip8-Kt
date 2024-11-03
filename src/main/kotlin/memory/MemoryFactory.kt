package com.tomassirio.memory

class MemoryFactory {
    fun createMemory(): Memory {
        return Memory(4096)
    }

    fun createETIMemory(): Memory {
        return Memory(4096, 0x600)
    }
}