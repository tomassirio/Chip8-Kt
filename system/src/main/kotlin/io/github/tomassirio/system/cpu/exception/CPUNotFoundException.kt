package io.github.tomassirio.system.cpu.exception

import io.github.tomassirio.system.cpu.CPUType

class CPUNotFoundException(cpuType: String) :
    IllegalArgumentException("CPU type '$cpuType' not found. Supported types are: ${supportedCPUs.joinToString(", ")}") {
    companion object {
        private val supportedCPUs = CPUType.entries.map { it.name }.sorted()
    }
}