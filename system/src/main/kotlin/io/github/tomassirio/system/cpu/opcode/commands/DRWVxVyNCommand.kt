package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.opcode.Command

/**
 * Dxyn - DRW Vx, Vy, nibble
 * Display n-byte sprite starting at memory location I at (Vx, Vy), set VF = collision.
 *
 * The interpreter reads n bytes from memory, starting at the address stored in I.
 * These bytes are then displayed as sprites on screen at coordinates (Vx, Vy).
 * Sprites are XORed onto the existing screen.
 * If this causes any pixels to be erased, VF is set to 1, otherwise it is set to 0.
 * If the sprite is positioned so part of it is outside the coordinates of the display,
 *      it wraps around to the opposite side of the screen.
 * See instruction 8xy3 for more information on XOR, and section 2.4,
 * Display, for more information on the Chip-8 screen and sprites.
 *
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun drwVxVyNCommand(): Command {
    return Command { cpu, opcode ->
        val x = cpu.registers[(opcode and 0x0F00u).toInt() shr 8].read().toInt()
        val y = cpu.registers[(opcode and 0x00F0u).toInt() shr 4].read().toInt()
        val nibble = (opcode and 0x000Fu).toInt()

        val spriteData = UByteArray(nibble)

        // Read n bytes starting from memory address I
        for (i in 0 until nibble) {
            spriteData[i] = cpu.memory.readByte((cpu.I.read() + i.toUInt()).toInt())
        }

        // Draw the sprite and check for collision
        val collision = cpu.displayState.drawSprite(x, y, spriteData)

        // Set VF register based on collision
        cpu.registers[0xF].write(if (collision) 1u else 0u)
    }
}