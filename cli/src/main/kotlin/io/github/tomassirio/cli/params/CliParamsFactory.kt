package io.github.tomassirio.cli.params

import io.github.tomassirio.system.cpu.CPUType
import org.springframework.boot.ApplicationArguments

object CliParamsFactory {
    private const val ROM_ARG = "rom"
    private const val CPU_ARG = "cpu"
    private const val FPS_ARG = "fps"

    fun fromArguments(args: ApplicationArguments): CliParams {
        val romPath = args.getOptionValues(ROM_ARG)?.firstOrNull()
            ?: throw IllegalArgumentException("A Rom is required to run the emulator. Pass it with --rom=<path>")
        val cpuType = args.getOptionValues(CPU_ARG)?.firstOrNull()?.let { CPUType.getByName(it) }
            ?: CPUType.CHIP8
        val fps = args.getOptionValues(FPS_ARG)?.firstOrNull()?.toIntOrNull() ?: 15

        return CliParams(romPath, cpuType, fps)
    }
}
