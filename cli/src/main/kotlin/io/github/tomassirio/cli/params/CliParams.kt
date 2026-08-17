package io.github.tomassirio.cli.params

import io.github.tomassirio.system.cpu.CPUType

data class CliParams(
    val romPath: String,
    val cpuType: CPUType,
    val fps: Int
)
