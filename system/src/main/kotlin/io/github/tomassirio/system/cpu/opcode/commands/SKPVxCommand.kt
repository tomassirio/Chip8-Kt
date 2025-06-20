package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.constants.KEYBOARD_KEYS
import io.github.tomassirio.system.cpu.opcode.Command

/**
 *  Ex9E - SKP Vx
 *  Skip next instruction if key with the value of Vx is pressed.
 *
 * Checks the keyboard, and if the key corresponding to the value of Vx is currently in the down position, PC is increased by 2.
 */
fun skpVxCommand(): Command {
    return Command { cpu, opcode ->
        val vx = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val vxValue = vx.read()

        val chip8Key = KEYBOARD_KEYS[vxValue.toInt()]
        val isPressed = cpu.keyboardState.isKeyPressed(chip8Key)

        if (isPressed) {
            cpu.pc.write(cpu.pc.read().plus(2u).toUShort())
        }
    }
}