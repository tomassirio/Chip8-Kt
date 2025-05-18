package io

import com.tomassirio.io.Keyboard
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class KeyboardTest {

    @Test
    fun `test key press and release`() {
        val keyboard = Keyboard()

        // Simulate key press
        keyboard.pressKey('A')
        assertThat(keyboard.isKeyPressed('A')).isTrue()

        // Simulate key release
        keyboard.releaseKey('A')
        assertThat(keyboard.isKeyPressed('A')).isFalse()
    }

    @Test
    fun `test multiple key presses`() {
        val keyboard = Keyboard()

        // Simulate multiple key presses
        keyboard.pressKey('A')
        keyboard.pressKey('B')
        assertThat(keyboard.isKeyPressed('A')).isTrue()
        assertThat(keyboard.isKeyPressed('B')).isTrue()

        // Release one key
        keyboard.releaseKey('A')
        assertThat(keyboard.isKeyPressed('A')).isFalse()
        assertThat(keyboard.isKeyPressed('B')).isTrue()
    }

    @Test
    fun `test only valid keys are pressed`() {
        val keyboard = Keyboard()

        "0123456789ABCDEF".forEach { key ->
            keyboard.pressKey(key)
            assertThat(keyboard.isKeyPressed(key)).isTrue()
        }
    }

    @Test
    fun `test invalid key press`() {
        val keyboard = Keyboard()

        // Simulate invalid key press
        keyboard.pressKey('G')
        assertThat(keyboard.isKeyPressed('G')).isFalse()
    }

    @Test
    fun `test clear keyboard state`() {
        val keyboard = Keyboard()

        // Simulate key presses
        keyboard.pressKey('A')
        keyboard.pressKey('B')

        // Clear the keyboard state
        keyboard.clear()

        // Check that all keys are released
        assertThat(keyboard.isKeyPressed('A')).isFalse()
        assertThat(keyboard.isKeyPressed('B')).isFalse()
    }
}