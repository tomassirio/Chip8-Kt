package com.tomassirio.system.cpu

import com.tomassirio.system.cpu.exception.CPUNotFoundException

enum class CPUType {
    CHIP8,
    ETI660;

    companion object {
        fun getByName(cpuType: String): CPUType {
            return entries.find { it.name.equals(cpuType, ignoreCase = true) }
                ?: throw CPUNotFoundException(cpuType)
        }
    }
}