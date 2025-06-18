package com.tomassirio.ui.mapping

import javafx.scene.input.KeyCode

class KeyMapper {
    fun mapToChip8Key(keyCode: KeyCode): Char? = keyMap[keyCode]

    private val keyMap = mapOf(
        KeyCode.DIGIT1 to '1',
        KeyCode.DIGIT2 to '2',
        KeyCode.DIGIT3 to '3',
        KeyCode.DIGIT4 to 'C',
        KeyCode.Q to '4',
        KeyCode.W to '5',
        KeyCode.E to '6',
        KeyCode.R to 'D',
        KeyCode.A to '7',
        KeyCode.S to '8',
        KeyCode.D to '9',
        KeyCode.F to 'E',
        KeyCode.Z to 'A',
        KeyCode.X to '0',
        KeyCode.C to 'B',
        KeyCode.V to 'F'
    )
}