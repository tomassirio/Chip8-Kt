package com.tomassirio.ui.params

import com.tomassirio.controller.debug.DebugLevel
import com.tomassirio.system.cpu.CPUType

data class EmulatorParams(
    val cpuType: CPUType,
    val debugMode: DebugLevel,
    val romPath: String,
    val scale: Int,
    val fps: Int
)
