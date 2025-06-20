package io.github.tomassirio.ui.params

import io.github.tomassirio.system.cpu.CPUType
import io.github.tomassirio.system.debug.DebugLevel

object EmulatorParamsFactory {
    private const val CPU_INPUT = "cpu"
    private const val DEBUG = "debug"
    private const val ROM_INPUT = "rom"
    private const val SCALE = "scale"
    private const val FPS = "fps"

    fun fromParams(params: Map<String, String>): EmulatorParams {
        return EmulatorParams(
            cpuType = CPUType.getByName(params[CPU_INPUT] ?: CPUType.CHIP8.name),
            debugMode = DebugLevel.getByName(params[DEBUG] ?: DebugLevel.NONE.name),
            romPath = params[ROM_INPUT] ?: throw IllegalArgumentException("A Rom is required to run the emulator"),
            scale = params[SCALE]?.toIntOrNull() ?: 10,
            fps = params[FPS]?.toIntOrNull() ?: 15
        )
    }
}