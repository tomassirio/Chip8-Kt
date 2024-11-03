package memory

import com.tomassirio.memory.Memory
import org.junit.jupiter.api.Test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class MemoryTest {

    private lateinit var memory: Memory

    @BeforeEach
    fun setup() {
        memory = Memory(4096)
    }

    @Test
    fun `read memory for Ubyte reads Byte in memory address`() {
        memory.write<UByte>(0, 0x69u)

        val read = memory.read<UByte>(0)
        assertThat(read).isEqualTo(0x69u.toUByte())
    }

    @Test
    fun `read memory for Ushort reads short in memory address`() {
        memory.write<UShort>(0, 0x6970u)

        val read = memory.read<UShort>(0)
        val readByte0 = memory.read<UByte>(0)
        val readByte1 = memory.read<UByte>(1)

        assertThat(read).isEqualTo(0x6970u.toUShort())
        assertThat(readByte0).isEqualTo(0x69u.toUByte())
        assertThat(readByte1).isEqualTo(0x70u.toUByte())
    }

    @Test
    fun `read memory for other type throws Exception`() {
        assertThrows<IllegalArgumentException> {
            memory.read<UInt>(0)
        }
    }

    @Test
    fun `write memory in preload memory after locking throws Exception`() {
        memory.lockBootSection()
        assertThrows<IllegalStateException> {
            memory.write(0, 0x69u)
        }
    }
}