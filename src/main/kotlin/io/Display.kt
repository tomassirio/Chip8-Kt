package com.tomassirio.io

import java.util.BitSet

@OptIn(ExperimentalUnsignedTypes::class)
class Display(val width: Int = 64, val height: Int = 32) {

    private val gfx: BitSet = BitSet(width * height)

    fun setPixel(x: Int, y: Int, value: Boolean) {
        val index = y * width + x
        gfx.set(index, value)
    }

    fun getPixel(x: Int, y: Int): Boolean {
        val index = y * width + x
        return gfx.get(index)
    }

    fun clearScreen() {
        gfx.clear()
    }

    /**
     * Draws a sprite at position (x,y) with the given sprite data.
     * Returns true if any pixels were erased (collision).
     */
    fun drawSprite(x: Int, y: Int, spriteData: UByteArray): Boolean {
        var collision = false

        for (row in spriteData.indices) {
            val yPos = (y + row) % height  // Wrap around vertically

            for (bit in 0 until Byte.SIZE_BITS) { // 8 bits per byte
                val xPos = (x + bit) % width  // Wrap around horizontally

                // Get bit from sprite byte (MSB first)
                val spriteBit = (spriteData[row].toInt() and (0x80 shr bit)) != 0

                if (spriteBit) { // Only process set bits
                    val currentPixel = getPixel(xPos, yPos)

                    // If pixel is already set, we'll have a collision when we flip it
                    if (currentPixel) {
                        collision = true
                    }

                    // XOR the pixel
                    setPixel(xPos, yPos, currentPixel xor true)
                }
            }
        }

        return collision
    }
}
