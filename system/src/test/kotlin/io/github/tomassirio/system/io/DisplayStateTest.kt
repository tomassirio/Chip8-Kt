package io.github.tomassirio.system.io

import io.github.tomassirio.system.io.display.DisplayState
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

    @Test
    fun `test scrollDown moves pixels down and clears top rows`() {
        // Set some pixels in top rows
        displayState.setPixel(1, 0, true)
        displayState.setPixel(2, 1, true)

        displayState.scrollDown(2)

        // Expect those pixels to move 2 rows down
        assertThat(displayState.getPixel(1, 2)).isTrue()
        assertThat(displayState.getPixel(2, 3)).isTrue()

        // Top rows should now be empty
        assertThat(displayState.getPixel(1, 0)).isFalse()
        assertThat(displayState.getPixel(2, 1)).isFalse()
    }

    @Test
    fun `test scrollLeft shifts pixels left and clears rightmost pixels`() {
        displayState.setPixel(5, 0, true)
        displayState.setPixel(6, 0, true)

        displayState.scrollLeft(2)

        assertThat(displayState.getPixel(3, 0)).isTrue()
        assertThat(displayState.getPixel(4, 0)).isTrue()

        // Rightmost pixels should now be false
        assertThat(displayState.getPixel(displayState.width - 1, 0)).isFalse()
        assertThat(displayState.getPixel(displayState.width - 2, 0)).isFalse()
    }

    @Test
    fun `test scrollRight shifts pixels right and clears leftmost pixels`() {
        displayState.setPixel(0, 0, true)
        displayState.setPixel(1, 0, true)

        displayState.scrollRight(2)

        assertThat(displayState.getPixel(2, 0)).isTrue()
        assertThat(displayState.getPixel(3, 0)).isTrue()

        // Leftmost should be cleared
        assertThat(displayState.getPixel(0, 0)).isFalse()
        assertThat(displayState.getPixel(1, 0)).isFalse()
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun `test clipping disables wrapped drawing`() {
        displayState.setClipping(true)
        val height = displayState.height
        val sprite = ubyteArrayOf(0b11111111u)

        // Drawing beyond the edge with clipping should skip those pixels
        displayState.drawSprite(0, height - 1, sprite)
        assertThat(displayState.getPixel(0, height - 1)).isTrue()
        assertThat(displayState.getPixel(1, height - 1)).isTrue()
        assertThat(displayState.getPixel(0, 0)).isFalse() // Should not wrap
    }

    @Test
    fun `test extended mode toggles resolution`() {
        displayState.enableExtendedMode()
        assertThat(displayState.isExtended()).isTrue()
        val extendedSize = displayState.width * displayState.height

        displayState.disableExtendedMode()
        assertThat(displayState.isExtended()).isFalse()
        val normalSize = displayState.width * displayState.height

        assertThat(extendedSize).isGreaterThan(normalSize)
    }

}