package io.github.tomassirio.cli

import io.github.tomassirio.cli.input.KeyHoldTracker
import io.github.tomassirio.cli.input.TerminalInputReader
import io.github.tomassirio.cli.loop.EmulatorLoop
import io.github.tomassirio.cli.mapping.KeyMapper
import io.github.tomassirio.cli.params.CliParamsFactory
import io.github.tomassirio.cli.render.TerminalRenderer
import io.github.tomassirio.controller.SystemController
import io.github.tomassirio.system.cpu.CPUType
import io.github.tomassirio.system.cpu.factory.CPUFactory
import io.github.tomassirio.system.io.display.DisplayType
import org.jline.terminal.Attributes
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import kotlin.system.exitProcess

private const val SHOW_CURSOR = "\u001B[?25h"

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
        val displayType = if (params.cpuType == CPUType.SCHIP8) DisplayType.SCHIP8 else DisplayType.CHIP8
        val requiredColumns = displayType.width * 2
        if (terminal.width < requiredColumns || terminal.height < displayType.height) {
            System.err.println(
                "Error: terminal is too small. Need at least ${requiredColumns}x${displayType.height} " +
                    "(columns x rows) for ${params.cpuType}, got ${terminal.width}x${terminal.height}."
            )
            terminal.close()
            exitProcess(1)
        }

        val originalAttributes = terminal.enterRawMode()
        val shutdownHook = Thread { restoreAttributesAndCursor(terminal, originalAttributes) }
        Runtime.getRuntime().addShutdownHook(shutdownHook)
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
            try {
                Runtime.getRuntime().removeShutdownHook(shutdownHook)
            } catch (e: IllegalStateException) {
                // JVM is already shutting down (hook already running/ran) — nothing to remove
            }
            restoreAttributesAndCursor(terminal, originalAttributes)
            terminal.close()
        }
    }

    private fun restoreAttributesAndCursor(terminal: Terminal, originalAttributes: Attributes) {
        terminal.writer().print(SHOW_CURSOR)
        terminal.writer().flush()
        terminal.attributes = originalAttributes
    }
}
