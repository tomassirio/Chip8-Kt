package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.CLSCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CLSCommandTest {

    private val clsCommand = CLSCommand
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `should clear the screen`() {
        // Given
        cpu.displayState.setPixel(0, 0, true)
        cpu.displayState.setPixel(1, 1, true)
        cpu.displayState.setPixel(2, 2, true)
        cpu.displayState.setPixel(3, 3, true)
        cpu.displayState.setPixel(4, 4, true)
        cpu.displayState.setPixel(5, 5, true)
        cpu.displayState.setPixel(6, 6, true)
        cpu.displayState.setPixel(7, 7, true)
        cpu.displayState.setPixel(8, 8, true)
        cpu.displayState.setPixel(9, 9, true)
        cpu.displayState.setPixel(10, 10, true)
        cpu.displayState.setPixel(11, 11, true)
        cpu.displayState.setPixel(12, 12, true)
        cpu.displayState.setPixel(13, 13, true)
        cpu.displayState.setPixel(14, 14, true)
        cpu.displayState.setPixel(15, 15, true)

        // When
        clsCommand.execute(cpu, 0x00E0u.toUShort())

        // Then
        for (x in 0 until cpu.displayState.width) {
            for (y in 0 until cpu.displayState.height) {
                assertThat(cpu.displayState.getPixel(x, y)).isFalse()
                    .withFailMessage("Pixel at ($x, $y) should be cleared")
            }
        }
    }
}