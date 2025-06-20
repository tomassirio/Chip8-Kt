package io.github.tomassirio.system.io.display

class DisplayModeManager(
    private val buffer: PixelBuffer,
    private var displayType: DisplayType
) {
    fun isExtended() = displayType == DisplayType.SCHIP8

    fun enableExtendedMode() {
        switchMode(DisplayType.SCHIP8)
    }

    fun disableExtendedMode() {
        switchMode(DisplayType.CHIP8)
    }

    private fun switchMode(type: DisplayType) {
        if (type != displayType) {
            displayType = type
            buffer.resize(displayType.width, displayType.height)
        }
    }
}