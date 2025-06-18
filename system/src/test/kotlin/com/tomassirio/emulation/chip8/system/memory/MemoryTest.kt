package com.tomassirio.emulation.chip8.system.memory

import com.tomassirio.emulation.chip8.system.memory.accessor.MemoryAccessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MemoryTest {

    private lateinit var memory: Memory

    @BeforeEach
    fun setup() {
        memory = Memory(4096)
    }

    @Test
    fun `read memory for Ubyte reads Byte in memory address`() {
        memory.writeByte(0, 0x69u)

        val read = memory.readByte(0)
        assertThat(read).isEqualTo(0x69u.toUByte())
    }

    @Test
    fun `read memory for Ushort reads short in memory address`() {
        MemoryAccessor.writeUShort(memory, 0, 0x6970u)

        val read = MemoryAccessor.readUShort(memory, 0)
        val readByte0 = memory.readByte(0)
        val readByte1 = memory.readByte(1)

        assertThat(read).isEqualTo(0x6970u.toUShort())
        assertThat(readByte0).isEqualTo(0x69u.toUByte())
        assertThat(readByte1).isEqualTo(0x70u.toUByte())
    }

    @Test
    fun `write memory in preload memory after locking throws Exception`() {
        memory.lockBootSection()
        assertThrows<IllegalStateException> {
            memory.writeByte(0, 0x69u)
        }
    }
}