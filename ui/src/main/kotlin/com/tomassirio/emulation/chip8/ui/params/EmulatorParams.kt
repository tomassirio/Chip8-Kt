package com.tomassirio.emulation.chip8.ui.params

import com.tomassirio.emulation.chip8.system.cpu.CPUType
import com.tomassirio.emulation.chip8.system.debug.DebugLevel

data class EmulatorParams(
    val cpuType: CPUType,
    val debugMode: DebugLevel,
    val romPath: String,
    val scale: Int,
    val fps: Int
)
