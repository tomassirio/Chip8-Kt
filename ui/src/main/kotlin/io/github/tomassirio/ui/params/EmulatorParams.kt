package io.github.tomassirio.ui.params

import io.github.tomassirio.system.cpu.CPUType
import io.github.tomassirio.system.debug.DebugLevel

data class EmulatorParams(
    val cpuType: CPUType,
    val debugMode: DebugLevel,
    val romPath: String,
    val scale: Int,
    val fps: Int
)
