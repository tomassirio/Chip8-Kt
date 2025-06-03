package com.tomassirio.ui

import com.tomassirio.controller.EmulatorController
import com.tomassirio.system.cpu.CPUType
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.io.ScreenMode
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import java.io.File
import kotlin.properties.Delegates

class EmulatorUI: Application() {
    private val CPU_INPUT = "cpu"
    private val SCREEN_INPUT = "screen"
    private val SCACLE = 10

    private var width by Delegates.notNull<Int>()
    private var height by Delegates.notNull<Int>()
    private lateinit var cpuType: CPUType

    private lateinit var emulatorController: EmulatorController
    private lateinit var gc: GraphicsContext
    private val ROM_INPUT = "rom"
    private lateinit var romPath: String

    override fun init() {
        val params = parameters.named
        val cpuTypeInput = params[CPU_INPUT] ?: CPUType.CHIP8.name
        val screenModeInput = params[SCREEN_INPUT] ?: ScreenMode.NORMAL.name
//        romPath = params[ROM_INPUT] ?: throw IllegalArgumentException("ROM file path is required!")
        romPath = "src/main/resources/roms/Airplane.ch8"

        cpuType = CPUType.getByName(cpuTypeInput)
        val screenMode = ScreenMode.getByName(screenModeInput)

        width = screenMode.width
        height = screenMode.height
    }

    override fun start(stage: Stage) {
        emulatorController = EmulatorController(CPUFactory.createCPU(cpuType))
        val romBytes = File(romPath).readBytes()
        emulatorController.loadRom(romBytes)

        val canvas = Canvas(width * SCACLE.toDouble(), height * SCACLE.toDouble())
        gc = canvas.graphicsContext2D

        val root = StackPane(canvas)
        val scene = Scene(root)

        scene.setOnKeyPressed { event -> handleKeyPress(event) }
        scene.setOnKeyReleased { event -> handleKeyRelease(event) }

        stage.scene = scene
        stage.title = "${cpuType.name} Emulator"
        stage.show()

        object : AnimationTimer() {
            override fun handle(now: Long) {
                emulatorController.tick()
                render()
//                debug()
            }
        }.start()
    }

    private fun debug() {
        gc.fillText("CPU PC: ${emulatorController.cpu.pc.read()}", 10.0, 20.0)
        gc.fillText("CPU SP: ${emulatorController.cpu.sp.read()}", 10.0, 40.0)
        gc.fillText("CPU I: ${emulatorController.cpu.I.read()}", 10.0, 60.0)
        gc.fillText("CPU DT: ${emulatorController.cpu.DT.read()}", 10.0, 80.0)
        gc.fillText("CPU ST: ${emulatorController.cpu.ST.read()}", 10.0, 100.0)
        gc.fillText("Waiting for key: ${emulatorController.cpu.waitingForKey}", 10.0, 120.0)
        emulatorController.cpu.registers.forEachIndexed { index, register ->
            gc.fillText("V[$index]: ${register.read()}", 10.0, 140.0 + index * 20)
        }
    }

    private fun render() {
        val pixels = emulatorController.cpu.displayState
        gc.clearRect(0.0, 0.0, width * SCACLE.toDouble(), height * SCACLE.toDouble())

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (pixels.getPixel(x,y)) {
                    gc.fillRect(x * SCACLE.toDouble(), y * SCACLE.toDouble(), SCACLE.toDouble(), SCACLE.toDouble())
                }
            }
        }
    }

    private fun handleKeyPress(event: KeyEvent) {
        val key = mapToChip8Key(event.code) ?: return
        emulatorController.onKeyPressed(key)
    }

    private fun handleKeyRelease(event: KeyEvent) {
        val key = mapToChip8Key(event.code) ?: return
        emulatorController.onKeyReleased(key)
    }

    private fun mapToChip8Key(keyCode: KeyCode): Char? {
        return when (keyCode) {
            KeyCode.DIGIT1 -> '1'
            KeyCode.DIGIT2 -> '2'
            KeyCode.DIGIT3 -> '3'
            KeyCode.DIGIT4 -> 'C'
            KeyCode.Q -> '4'
            KeyCode.W -> '5'
            KeyCode.E -> '6'
            KeyCode.R -> 'D'
            KeyCode.A -> '7'
            KeyCode.S -> '8'
            KeyCode.D -> '9'
            KeyCode.F -> 'E'
            KeyCode.Z -> 'A'
            KeyCode.X -> '0'
            KeyCode.C -> 'B'
            KeyCode.V -> 'F'
            else -> null
        }
    }

}