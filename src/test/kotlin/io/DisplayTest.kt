package io

import com.tomassirio.io.Display
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DisplayTest {
    private lateinit var display: Display

    @BeforeEach
    fun setUp() {
        display = Display()
    }

    @Test
    fun `test setPixel`() {
        val x = 10
        val y = 5

        display.setPixel(x, y, true)
        assertThat(display.getPixel(x, y)).isTrue()
    }

    @Test
    fun `test getPixel`() {
        val x = 10
        val y = 5

        display.setPixel(x, y, true)
        assertThat(display.getPixel(x, y)).isTrue()

        display.setPixel(x, y, false)
        assertThat(display.getPixel(x, y)).isFalse()
    }

    @Test
    fun `test clear`() {
        display.setPixel(10, 5, true)
        display.clearScreen()
        for (displayX in 0 until display.width) {
            for (displayY in 0 until display.height) {
                assertThat(display.getPixel(displayX, displayY)).isFalse()
            }
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun `test drawSprite`() {
        val x = 10
        val y = 5
        val sprite = ubyteArrayOf(0b11111111u, 0b00000000u)

        display.drawSprite(x, y, sprite)

        // Check that the pixels were set correctly
        for (i in sprite.indices) {
            for (j in 0 until Byte.SIZE_BITS) {
                val expected = (sprite[i].toInt() shr (7 - j)) and 1 == 1
                assertThat(display.getPixel(x + j, y + i)).isEqualTo(expected)
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
        display.setPixel(x + 1, y, true)

        // Draw the sprite
        val collision = display.drawSprite(x, y, sprite)

        // Check that the collision was detected
        assertThat(collision).isTrue()
    }
}