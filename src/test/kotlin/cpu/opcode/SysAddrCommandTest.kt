package cpu.opcode

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.CPUFactory
import com.tomassirio.cpu.opcode.SysAddrCommand
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SysAddrCommandTest {

    private val sysAddrCommand = SysAddrCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `should set I register to the address from opcode`() {
        // Given
        val opcode: UShort = 0x0123u.toUShort()

        // When
        sysAddrCommand.execute(cpu, opcode)

        // Then
        assertEquals(0x0123u.toUShort(), cpu.I.value,
            "I register should contain the address 0x0123 from opcode")
    }

    @Test
    fun `should ignore upper nibble of opcode`() {
        // Given
        val opcode: UShort = 0xF123u.toUShort()  // Upper nibble should be ignored

        // When
        sysAddrCommand.execute(cpu, opcode)

        // Then
        assertEquals(0x0123u.toUShort(), cpu.I.value,
            "I register should only contain lower 12 bits of the address")
    }
}