package io.github.tomassirio.ui


import io.github.tomassirio.controller.SystemController
import io.github.tomassirio.system.cpu.factory.CPUFactory
import io.github.tomassirio.system.debug.DebugLevel
import io.github.tomassirio.system.io.display.DisplayType
import io.github.tomassirio.ui.constants.NANOS_PER_FRAME
import io.github.tomassirio.ui.constants.TARGET_FPS
import io.github.tomassirio.ui.debug.Debugger
import io.github.tomassirio.ui.pane.createDebuggingSplitPane
import io.github.tomassirio.ui.params.EmulatorParams
import io.github.tomassirio.ui.params.EmulatorParamsFactory
import io.github.tomassirio.ui.scene.createMainScene
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.io.File
import kotlin.properties.Delegates

data class MemoryRow(val address: String, val value: String)

class EmulatorUI : Application() {
    private var width by Delegates.notNull<Int>()
    private var height by Delegates.notNull<Int>()

    private lateinit var emulatorParams: EmulatorParams
    private lateinit var systemController: SystemController
    private lateinit var debugMode: DebugLevel
    private lateinit var debugger: Debugger
    private lateinit var gc: GraphicsContext

    private val debugFields = mutableMapOf<String, TextField>()
    private val memoryTable = TableView<MemoryRow>()
    private val memoryItems = FXCollections.observableArrayList<MemoryRow>()


    override fun init() {
        emulatorParams = EmulatorParamsFactory.fromParams(parameters.named)
        width = DisplayType.CHIP8.width
        height = DisplayType.CHIP8.height
        debugMode = emulatorParams.debugMode
    }

    override fun start(stage: Stage) {
        setupSystem()
        debugger = Debugger(debugFields, memoryTable, memoryItems, systemController)
        val (scene, graphicContext) = createMainScene(systemController, width, height, emulatorParams.scale)
        gc = graphicContext
        stage.scene = scene
        stage.title = "${emulatorParams.cpuType.name} Emulator"
        stage.show()

        createDebuggingSplitPane(
            stage,
            debugFields,
            memoryTable,
            memoryItems,
            systemController,
            debugMode
        )
        startEmulatorLoop()
    }

    private fun setupSystem() {
        systemController = SystemController(CPUFactory.createCPU(emulatorParams.cpuType))
        val romBytes = File(emulatorParams.romPath).readBytes()
        systemController.loadRom(romBytes)
    }

    private fun startEmulatorLoop() {
        val targetFps = TARGET_FPS
        val nanosPerFrame = NANOS_PER_FRAME / targetFps
        val cyclesPerFrame = emulatorParams.fps

        var lastFrameTime = System.nanoTime()

        object : AnimationTimer() {
            override fun handle(now: Long) {
                if (now - lastFrameTime >= nanosPerFrame) {
                    repeat(cyclesPerFrame) {
                        systemController.tick()
                    }
                    render()

                    if (debugMode != DebugLevel.NONE) {
                        debugger.debug(debugMode)
                    }

                    lastFrameTime = now
                }
            }
        }.start()
    }

    private fun render() {
        val pixels = systemController.getDisplayState()
        if (pixels.isExtended()) {
            width = DisplayType.SCHIP8.width
            height = DisplayType.SCHIP8.height
        }
        gc.clearRect(
            0.0,
            0.0,
            width * emulatorParams.scale.toDouble(),
            height * emulatorParams.scale.toDouble()
        )

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (pixels.getPixel(x, y)) {
                    gc.fillRect(
                        x * emulatorParams.scale.toDouble(),
                        y * emulatorParams.scale.toDouble(),
                        emulatorParams.scale.toDouble(),
                        emulatorParams.scale.toDouble()
                    )
                }
            }
        }
    }
}