package com.tomassirio.system.io

import com.tomassirio.system.io.exception.ScreenModeNotFoundException

enum class ScreenMode(val width: Int, val height: Int) {
    NORMAL(64, 32),
    WIDE(64, 48),
    BIG(64, 64);

    companion object {
        fun getByName(name: String): ScreenMode {
            return entries.find { it.name.equals(name, ignoreCase = true) }
                ?: throw ScreenModeNotFoundException(name)
        }
    }
}