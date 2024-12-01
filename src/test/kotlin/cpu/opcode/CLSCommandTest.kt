package cpu.opcode

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.CPUFactory
import com.tomassirio.cpu.opcode.CLSCommand
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
        cpu.display.setPixel(0, 0, true)
        cpu.display.setPixel(1, 1, true)
        cpu.display.setPixel(2, 2, true)
        cpu.display.setPixel(3, 3, true)
        cpu.display.setPixel(4, 4, true)
        cpu.display.setPixel(5, 5, true)
        cpu.display.setPixel(6, 6, true)
        cpu.display.setPixel(7, 7, true)
        cpu.display.setPixel(8, 8, true)
        cpu.display.setPixel(9, 9, true)
        cpu.display.setPixel(10, 10, true)
        cpu.display.setPixel(11, 11, true)
        cpu.display.setPixel(12, 12, true)
        cpu.display.setPixel(13, 13, true)
        cpu.display.setPixel(14, 14, true)
        cpu.display.setPixel(15, 15, true)

        // When
        clsCommand.execute(cpu, 0x00E0u.toUShort())

        // Then
        for (x in 0 until cpu.display.width) {
            for (y in 0 until cpu.display.height) {
                assertThat(cpu.display.getPixel(x, y)).isFalse()
                    .withFailMessage("Pixel at ($x, $y) should be cleared")
            }
        }
    }
}