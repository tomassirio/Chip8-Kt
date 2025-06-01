package system.io

import com.tomassirio.system.io.DisplayState
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DisplayStateTest {
    private lateinit var displayState: DisplayState

    @BeforeEach
    fun setUp() {
        displayState = DisplayState()
    }

    @Test
    fun `test setPixel`() {
        val x = 10
        val y = 5

        displayState.setPixel(x, y, true)
        assertThat(displayState.getPixel(x, y)).isTrue()
    }

    @Test
    fun `test getPixel`() {
        val x = 10
        val y = 5

        displayState.setPixel(x, y, true)
        assertThat(displayState.getPixel(x, y)).isTrue()

        displayState.setPixel(x, y, false)
        assertThat(displayState.getPixel(x, y)).isFalse()
    }

    @Test
    fun `test clear`() {
        displayState.setPixel(10, 5, true)
        displayState.clear()
        for (displayX in 0 until displayState.width) {
            for (displayY in 0 until displayState.height) {
                assertThat(displayState.getPixel(displayX, displayY)).isFalse()
            }
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun `test drawSprite`() {
        val x = 10
        val y = 5
        val sprite = ubyteArrayOf(0b11111111u, 0b00000000u)

        displayState.drawSprite(x, y, sprite)

        // Check that the pixels were set correctly
        for (i in sprite.indices) {
            for (j in 0 until Byte.SIZE_BITS) {
                val expected = (sprite[i].toInt() shr (7 - j)) and 1 == 1
                assertThat(displayState.getPixel(x + j, y + i)).isEqualTo(expected)
            }
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun `test drawSprite when collision occurs`() {
        val x = 10
        val y = 5
        val sprite = ubyteArrayOf(0b11111111u, 0b00000000u)

        // Set some pixels on the screen to create a collision
        displayState.setPixel(x + 1, y, true)

        // Draw the sprite
        val collision = displayState.drawSprite(x, y, sprite)

        // Check that the collision was detected
        assertThat(collision).isTrue()
    }
}