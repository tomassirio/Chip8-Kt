package com.tomassirio.controller.debug

enum class DebugLevel {
    NONE,
    MINIMAL,
    FULL;

    companion object {
        fun getByName(name: String): DebugLevel {
            return DebugLevel.entries.find { it.name.equals(name, ignoreCase = true) }
                ?: throw IllegalArgumentException(name)
        }
    }
}