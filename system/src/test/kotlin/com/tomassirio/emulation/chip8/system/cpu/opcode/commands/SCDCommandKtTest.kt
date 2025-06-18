package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.CPU
import com.tomassirio.emulation.chip8.system.cpu.CPUType
import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SCDNCommandKtTest {
    private val command = scdNCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
        cpu.displayState.clear()

        // Draw a horizontal line at y = 0
        for (x in 0 until cpu.displayState.width) {
            cpu.displayState.setPixel(x, 0, true)
        }
    }

    @Test
    fun testScdNCommandScrollsDisplayDown() {
        // Given
        val opcode: UShort = 0x00C2u // Scroll down 2 rows

        // When
        command.execute(cpu, opcode)

        // Then
        for (x in 0 until cpu.displayState.width) {
            // Row 0 and 1 should now be clear
            assertThat(cpu.displayState.getPixel(x, 0)).isFalse()
            assertThat(cpu.displayState.getPixel(x, 1)).isFalse()

            // Original row 0 should now be at row 2
            assertThat(cpu.displayState.getPixel(x, 2)).isTrue()
        }
    }
}
