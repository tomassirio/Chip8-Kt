package io.github.tomassirio.cli.input

class KeyHoldTracker(private val timeoutMillis: Long = 100L) {
    private val lastSeen = mutableMapOf<Char, Long>()

    fun keyReceived(key: Char, now: Long): Boolean {
        val isNewPress = !lastSeen.containsKey(key)
        lastSeen[key] = now
        return isNewPress
    }

    fun releaseExpired(now: Long): List<Char> {
        val expired = lastSeen.filterValues { now - it > timeoutMillis }.keys.toList()
        expired.forEach { lastSeen.remove(it) }
        return expired
    }
}
