package io.github.tomassirio.cli.mapping

// Physical layout mirrored in io.github.tomassirio.ui.mapping.KeyMapper (JavaFX KeyCode-based) — keep both in sync.
class KeyMapper {
    fun mapToChip8Key(key: Char): Char? = keyMap[key.lowercaseChar()]

    private val keyMap = mapOf(
        '1' to '1', '2' to '2', '3' to '3', '4' to 'C',
        'q' to '4', 'w' to '5', 'e' to '6', 'r' to 'D',
        'a' to '7', 's' to '8', 'd' to '9', 'f' to 'E',
        'z' to 'A', 'x' to '0', 'c' to 'B', 'v' to 'F'
    )
}
