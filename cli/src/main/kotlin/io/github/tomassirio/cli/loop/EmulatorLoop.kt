package io.github.tomassirio.cli.loop

import io.github.tomassirio.cli.input.KeyHoldTracker
import io.github.tomassirio.cli.input.TerminalInputReader
import io.github.tomassirio.cli.mapping.KeyMapper
import io.github.tomassirio.cli.render.TerminalRenderer
import io.github.tomassirio.controller.SystemController

private const val TARGET_FPS = 60
private const val NANOS_PER_FRAME = 1_000_000_000L / TARGET_FPS
private const val ESCAPE = 27.toChar()

class EmulatorLoop(
    private val systemController: SystemController,
    private val renderer: TerminalRenderer,
    private val inputReader: TerminalInputReader,
    private val keyMapper: KeyMapper,
    private val keyHoldTracker: KeyHoldTracker,
    private val cyclesPerFrame: Int
) {
    fun run() {
        var lastFrameTime = System.nanoTime()
        var running = true

        while (running) {
            val now = System.nanoTime()
            if (now - lastFrameTime >= NANOS_PER_FRAME) {
                repeat(cyclesPerFrame) { systemController.tick() }
                renderer.render(systemController.getDisplayState())
                running = handleInput()
                lastFrameTime = now
            } else {
                Thread.sleep(1)
            }
        }
    }

    private fun handleInput(): Boolean {
        val nowMillis = System.currentTimeMillis()

        for (char in inputReader.pollChars()) {
            if (char == 'q' || char == ESCAPE) return false

            val chip8Key = keyMapper.mapToChip8Key(char) ?: continue
            if (keyHoldTracker.keyReceived(chip8Key, nowMillis)) {
                systemController.onKeyPressed(chip8Key)
            }
        }

        keyHoldTracker.releaseExpired(nowMillis).forEach { systemController.onKeyReleased(it) }
        return true
    }
}
