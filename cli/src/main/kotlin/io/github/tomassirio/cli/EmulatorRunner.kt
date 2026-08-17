package io.github.tomassirio.cli

import io.github.tomassirio.cli.input.KeyHoldTracker
import io.github.tomassirio.cli.input.TerminalInputReader
import io.github.tomassirio.cli.loop.EmulatorLoop
import io.github.tomassirio.cli.mapping.KeyMapper
import io.github.tomassirio.cli.params.CliParamsFactory
import io.github.tomassirio.cli.render.TerminalRenderer
import io.github.tomassirio.controller.SystemController
import io.github.tomassirio.system.cpu.factory.CPUFactory
import org.jline.terminal.TerminalBuilder
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import kotlin.system.exitProcess

@Component
class EmulatorRunner : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        val params = try {
            CliParamsFactory.fromArguments(args)
        } catch (e: IllegalArgumentException) {
            System.err.println("Error: ${e.message}")
            exitProcess(1)
        }

        val systemController = SystemController(CPUFactory.createCPU(params.cpuType))
        systemController.loadRom(File(params.romPath).readBytes())

        val terminal = TerminalBuilder.builder().system(true).build()
        val originalAttributes = terminal.enterRawMode()
        try {
            EmulatorLoop(
                systemController,
                TerminalRenderer(terminal.writer()),
                TerminalInputReader(terminal.reader()),
                KeyMapper(),
                KeyHoldTracker(),
                params.fps
            ).run()
        } finally {
            terminal.attributes = originalAttributes
            terminal.close()
        }
    }
}
