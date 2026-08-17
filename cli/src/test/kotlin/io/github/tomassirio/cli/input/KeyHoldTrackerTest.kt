package io.github.tomassirio.cli.input

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KeyHoldTrackerTest {

    @Test
    fun `keyReceived returns true for a new press and false while still held`() {
        val tracker = KeyHoldTracker(timeoutMillis = 100L)

        assertThat(tracker.keyReceived('A', now = 0L)).isTrue()
        assertThat(tracker.keyReceived('A', now = 10L)).isFalse()
    }

    @Test
    fun `releaseExpired keeps key pressed within timeout`() {
        val tracker = KeyHoldTracker(timeoutMillis = 100L)
        tracker.keyReceived('A', now = 0L)

        val expired = tracker.releaseExpired(now = 50L)

        assertThat(expired).isEmpty()
    }

    @Test
    fun `releaseExpired releases key after timeout with no refresh`() {
        val tracker = KeyHoldTracker(timeoutMillis = 100L)
        tracker.keyReceived('A', now = 0L)

        val expired = tracker.releaseExpired(now = 150L)

        assertThat(expired).containsExactly('A')
    }

    @Test
    fun `key can be pressed again as new press after being released`() {
        val tracker = KeyHoldTracker(timeoutMillis = 100L)
        tracker.keyReceived('A', now = 0L)
        tracker.releaseExpired(now = 150L)

        assertThat(tracker.keyReceived('A', now = 200L)).isTrue()
    }
}
