package com.tomassirio.emulation.chip8

import com.tomassirio.emulation.chip8.ui.EmulatorUI
import javafx.application.Application

fun main(args: Array<String>) {
    Application.launch(EmulatorUI::class.java, *args)
}