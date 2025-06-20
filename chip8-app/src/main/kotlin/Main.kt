package io.github.tomassirio

import io.github.tomassirio.ui.EmulatorUI
import javafx.application.Application

fun main(args: Array<String>) {
    Application.launch(EmulatorUI::class.java, *args)
}