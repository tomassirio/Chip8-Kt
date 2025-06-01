package system.memory

import com.tomassirio.system.memory.Memory
import com.tomassirio.system.memory.util.toUByteAt
import com.tomassirio.system.memory.util.toUShortAt
import org.junit.jupiter.api.Test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalUnsignedTypes::class)
class MemoryTest {

    private lateinit var memory: Memory

    @BeforeEach
    fun setup() {
        memory = Memory(4096)
    }

    @Test
    fun `read memory for Ubyte reads Byte in memory address`() {
        memory.write(0, 0x69u)

        val read = memory.read(0) { bytes, addr -> bytes.toUByteAt(addr) }
        assertThat(read).isEqualTo(0x69u.toUByte())
    }

    @Test
    fun `read memory for Ushort reads short in memory address`() {
        memory.write(0, 0x6970u)

        val read = memory.read(0) { bytes, addr -> bytes.toUShortAt(addr) }
        val readByte0 = memory.read(0) { bytes, addr -> bytes.toUByteAt(addr) }
        val readByte1 = memory.read(1) { bytes, addr -> bytes.toUByteAt(addr) }

        assertThat(read).isEqualTo(0x6970u.toUShort())
        assertThat(readByte0).isEqualTo(0x69u.toUByte())
        assertThat(readByte1).isEqualTo(0x70u.toUByte())
    }

    @Test
    fun `write memory in preload memory after locking throws Exception`() {
        memory.lockBootSection()
        assertThrows<IllegalStateException> {
            memory.write(0, 0x69u)
        }
    }
}