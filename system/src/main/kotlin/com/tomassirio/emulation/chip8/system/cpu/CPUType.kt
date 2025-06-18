package com.tomassirio.emulation.chip8.system.cpu

import com.tomassirio.emulation.chip8.system.cpu.exception.CPUNotFoundException

enum class CPUType {
    CHIP8,
    SCHIP8;

    companion object {
        fun getByName(cpuType: String): CPUType {
            return entries.find { it.name.equals(cpuType, ignoreCase = true) }
                ?: throw CPUNotFoundException(cpuType)
        }
    }
}