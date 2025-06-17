package com.tomassirio.system.io.display

enum class DisplayType(val width: Int, val height: Int) {
    CHIP8(64, 32),
    SCHIP8(128, 64);

    companion object {
        fun getByName(displayType: String): DisplayType {
            return DisplayType.entries.find { it.name.equals(displayType, ignoreCase = true) }
                ?: throw IllegalArgumentException(displayType)
        }
    }
}