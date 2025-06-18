package com.tomassirio.emulation.chip8.system.cpu.opcode.commands

import com.tomassirio.emulation.chip8.system.cpu.CPU
import com.tomassirio.emulation.chip8.system.cpu.CPUType
import com.tomassirio.emulation.chip8.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SCLCommandKtTest {
    private val command = sclCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
        cpu.displayState.clear()

        // Draw a vertical line at x = 4
        for (y in 0 until cpu.displayState.height) {
            cpu.displayState.setPixel(4, y, true)
        }
    }

    @Test
    fun testSclCommandScrollsDisplayLeftBy4() {
        // When
        command.execute(cpu, 0x00FCu)

        // Then
        for (y in 0 until cpu.displayState.height) {
            // Pixel originally at x=4 should now be at x=0
            assertThat(cpu.displayState.getPixel(0, y)).isTrue()

            // x=1..3 should be false (was empty)
            for (x in 1..3) {
                assertThat(cpu.displayState.getPixel(x, y)).isFalse()
            }

            // Rightmost 4 pixels should be clear
            for (x in cpu.displayState.width - 4 until cpu.displayState.width) {
                assertThat(cpu.displayState.getPixel(x, y)).isFalse()
            }
        }
    }
}
