package system.io

import com.tomassirio.system.io.KeyboardState
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class KeyboardStateTest {

    @Test
    fun `test key press and release`() {
        val keyboardState = KeyboardState()

        // Simulate key press
        keyboardState.pressKey('A')
        assertThat(keyboardState.isKeyPressed('A')).isTrue()

        // Simulate key release
        keyboardState.releaseKey('A')
        assertThat(keyboardState.isKeyPressed('A')).isFalse()
    }

    @Test
    fun `test multiple key presses`() {
        val keyboardState = KeyboardState()

        // Simulate multiple key presses
        keyboardState.pressKey('A')
        keyboardState.pressKey('B')
        assertThat(keyboardState.isKeyPressed('A')).isTrue()
        assertThat(keyboardState.isKeyPressed('B')).isTrue()

        // Release one key
        keyboardState.releaseKey('A')
        assertThat(keyboardState.isKeyPressed('A')).isFalse()
        assertThat(keyboardState.isKeyPressed('B')).isTrue()
    }

    @Test
    fun `test only valid keys are pressed`() {
        val keyboardState = KeyboardState()

        "0123456789ABCDEF".forEach { key ->
            keyboardState.pressKey(key)
            assertThat(keyboardState.isKeyPressed(key)).isTrue()
        }
    }

    @Test
    fun `test invalid key press`() {
        val keyboardState = KeyboardState()

        // Simulate invalid key press
        keyboardState.pressKey('G')
        assertThat(keyboardState.isKeyPressed('G')).isFalse()
    }

    @Test
    fun `test clear keyboard state`() {
        val keyboardState = KeyboardState()

        // Simulate key presses
        keyboardState.pressKey('A')
        keyboardState.pressKey('B')

        // Clear the keyboard state
        keyboardState.clear()

        // Check that all keys are released
        assertThat(keyboardState.isKeyPressed('A')).isFalse()
        assertThat(keyboardState.isKeyPressed('B')).isFalse()
    }
}