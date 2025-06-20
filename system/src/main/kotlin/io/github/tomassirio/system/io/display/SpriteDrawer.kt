package io.github.tomassirio.system.io.display

class SpriteDrawer(
    private val buffer: PixelBuffer,
    private val width: Int,
    private val height: Int,
    private val clippingEnabled: Boolean
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun draw(x: Int, y: Int, data: UByteArray): Boolean {
        var collision = false

        data.forEachIndexed { row, byte ->
            val yPos = resolveY(y + row) ?: return@forEachIndexed

            repeat(8) { bit ->
                val xPos = resolveX(x + bit) ?: return@repeat
                if ((byte.toInt() and (0x80 shr bit)) != 0) {
                    val existing = buffer.get(xPos, yPos)
                    buffer.set(xPos, yPos, !existing)
                    collision = collision || existing
                }
            }
        }
        return collision
    }

    private fun resolveX(x: Int): Int? =
        if (clippingEnabled && x >= width) null else x % width

    private fun resolveY(y: Int): Int? =
        if (clippingEnabled && y >= height) null else y % height
}
