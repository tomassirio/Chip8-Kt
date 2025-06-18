package com.tomassirio.emulation.chip8.ui.scene

import com.tomassirio.emulation.chip8.controller.SystemController
import com.tomassirio.emulation.chip8.ui.mapping.KeyMapper
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.StackPane

fun createMainScene(
    systemController: SystemController,
    width: Int,
    height: Int,
    scale: Int
): Pair<Scene, GraphicsContext> {
    val keyMapper = KeyMapper()
    val canvas = Canvas(width * scale.toDouble(), height * scale.toDouble())
    val gc = canvas.graphicsContext2D

    val root = StackPane(canvas)
    val scene = Scene(root)

    scene.setOnKeyPressed { event ->
        keyMapper.mapToChip8Key(event.code)?.let { systemController.onKeyPressed(it) }
    }

    scene.setOnKeyReleased { event ->
        keyMapper.mapToChip8Key(event.code)?.let { systemController.onKeyReleased(it) }
    }
    return scene to gc
}