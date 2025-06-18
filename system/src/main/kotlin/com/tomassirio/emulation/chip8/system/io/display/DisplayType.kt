package com.tomassirio.emulation.chip8.system.io.display

enum class DisplayType(val width: Int, val height: Int) {
    CHIP8(64, 32),
    SCHIP8(128, 64);
}