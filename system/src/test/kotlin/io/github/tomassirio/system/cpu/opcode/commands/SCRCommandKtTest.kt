package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.CPU
import io.github.tomassirio.system.cpu.CPUType
import io.github.tomassirio.system.cpu.factory.CPUFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SCRCommandKtTest {
    private val command = scrCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setUp() {
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
        cpu.displayState.clear()

        // Draw a vertical line at x = 0
        for (y in 0 until cpu.displayState.height) {
            cpu.displayState.setPixel(0, y, true)
        }
    }

    @Test
    fun testScrCommandScrollsDisplayRightBy4() {
        // When
        command.execute(cpu, 0x00FBu)

        // Then
        for (y in 0 until cpu.displayState.height) {
            // Pixel originally at x=0 should now be at x=4
            assertThat(cpu.displayState.getPixel(4, y)).isTrue()

            // Pixels at x=0 to x=3 should now be false (cleared)
            for (x in 0..3) {
                assertThat(cpu.displayState.getPixel(x, y)).isFalse()
            }
        }
    }
}
