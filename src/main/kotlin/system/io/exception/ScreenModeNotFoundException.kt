package com.tomassirio.system.io.exception

import com.tomassirio.system.io.ScreenMode

class ScreenModeNotFoundException (screenMode: String) :
    IllegalArgumentException("Screen resolution '$screenMode' not found. Supported types are: ${supportedCPUs.joinToString(", ")}") {
    companion object {
        private val supportedCPUs = ScreenMode.entries.map { it.name }.sorted()
    }
}