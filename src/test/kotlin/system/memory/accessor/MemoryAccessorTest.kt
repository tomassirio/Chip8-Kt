package system.memory.accessor

import com.tomassirio.system.memory.Memory
import com.tomassirio.system.memory.accessor.MemoryAccessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MemoryAccessorTest {

    private lateinit var memory: Memory

    @BeforeEach
    fun setup() {
        memory = Memory(4096)
    }

    @Test
    fun `writeUShort writes the correct bytes to memory`() {
        val address = 100
        val value: UShort = 0xABCDu

        MemoryAccessor.writeUShort(memory, address, value)

        val byte0 = memory.readByte(address)
        val byte1 = memory.readByte(address + 1)

        assertThat(byte0).isEqualTo(0xABu.toUByte())
        assertThat(byte1).isEqualTo(0xCDu.toUByte())
    }

    @Test
    fun `readUShort reads the correct value from memory`() {
        val address = 200
        memory.writeByte(address, 0x12u)
        memory.writeByte(address + 1, 0x34u)

        val result = MemoryAccessor.readUShort(memory, address)

        assertThat(result).isEqualTo(0x1234u.toUShort())
    }

    @Test
    fun `readUShort after writeUShort returns the original value`() {
        val address = 300
        val original: UShort = 0xDEADu

        MemoryAccessor.writeUShort(memory, address, original)
        val result = MemoryAccessor.readUShort(memory, address)

        assertThat(result).isEqualTo(original)
    }
}