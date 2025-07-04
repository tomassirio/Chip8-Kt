package io.github.tomassirio.controller

import io.github.tomassirio.controller.sfx.SoundPlayer
import io.github.tomassirio.system.cpu.CPU
import io.github.tomassirio.controller.debug.CPUDebugger
import io.github.tomassirio.controller.handler.KeyHandler
import io.github.tomassirio.controller.loader.RomLoader
import io.github.tomassirio.system.memory.accessor.MemoryAccessor

class SystemController(
    private val cpu: CPU
) {
    val debugger = CPUDebugger(
        cpu.memory, cpu.registers, cpu.pc, cpu.I,
        cpu.DT, cpu.ST, cpu.keyboardState, cpu.stack
    )
    private val keyHandler = KeyHandler(cpu.keyboardState, cpu.registers)
    private val romLoader = RomLoader(cpu.memory)
    private val soundPlayer = SoundPlayer()

    fun loadRom(data: ByteArray) {
        romLoader.loadRom(data)
    }

    fun tick() {
        // Handle system-level concerns
        if (cpu.keyboardState.isWaitingForKey) {
            debugger.logWaitingForKey()
            updateTimers()
            return
        }

        debugger.incrementCycle()

        val opcodeBefore = peekNextOpcode()

        debugger.logCycleStart(opcodeBefore)
        cpu.runCycle()
        debugger.logPostExecution(cpu.keyboardState.isWaitingForKey)

        updateTimers()
    }

    fun getDisplayState() = cpu.displayState

    private fun updateTimers() {
        cpu.DT.tick()

        val stBefore = cpu.ST.read()
        cpu.ST.tick()
        val stAfter = cpu.ST.read()

        if (stAfter > 0u) {
            soundPlayer.play()
        } else if (stBefore > 0u && stAfter.toUInt() == 0u) {
            soundPlayer.stop()
        }
    }

    private fun peekNextOpcode(): UShort {
        return MemoryAccessor.readUShort(cpu.memory, cpu.pc.read().toInt())
    }

    fun onKeyPressed(chip8Key: Char) {
        keyHandler.onKeyPressed(chip8Key)
    }

    fun onKeyReleased(chip8Key: Char) {
        keyHandler.onKeyReleased(chip8Key)
    }
}