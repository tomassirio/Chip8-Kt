package com.tomassirio.emulation.chip8.system.io.display

class DisplayState {
    private var displayType = DisplayType.CHIP8
    private var clipping = false
    private val buffer = PixelBuffer(displayType.width, displayType.height)
    private val drawer = SpriteDrawer(buffer, width, height, clipping)
    private val scroller = DisplayScroller(buffer, width, height)
    private val modeManager = DisplayModeManager(buffer, displayType)

    val width get() = buffer.width
    val height get() = buffer.height

    fun setPixel(x: Int, y: Int, value: Boolean) = buffer.set(x, y, value)
    fun getPixel(x: Int, y: Int): Boolean = buffer.get(x, y)
    fun clear() = buffer.clear()

    @OptIn(ExperimentalUnsignedTypes::class)
    fun drawSprite(x: Int, y: Int, sprite: UByteArray): Boolean = drawer.draw(x, y, sprite)

    fun setClipping(enabled: Boolean) {
        clipping = enabled
    }

    fun scrollDown(lines: Int) = scroller.scrollDown(lines)

    fun scrollRight(pixels: Int) = scroller.scrollRight(pixels)

    fun scrollLeft(pixels: Int) = scroller.scrollLeft(pixels)

    fun isExtended() = modeManager.isExtended()

    fun enableExtendedMode() = modeManager.enableExtendedMode()

    fun disableExtendedMode() = modeManager.disableExtendedMode()
}

