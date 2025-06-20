package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.factory.CPUFactory.createCPU
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ADDVxByteCommandTest {

    @Test
    fun `execute should add kk to Vx`() {
        // Given
        val cpu = createCPU()
        val register = cpu.registers[0]
        register.write(0x01u)
        val opcode = 0x7001u.toUShort()

        // When
        addVxByteCommand().execute(cpu, opcode)

        // Then
        assertEquals(0x02u.toUByte(), register.read())
    }
}