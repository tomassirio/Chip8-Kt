package com.tomassirio.cpu.opcode.commands

import com.tomassirio.cpu.CPU
import com.tomassirio.cpu.opcode.Command
import kotlin.random.Random

/**
 *   Cxkk - RND Vx, byte
 *   Set Vx = random byte AND kk.
 *
 *   The interpreter generates a random number from 0 to 255, which is then ANDed with the value kk.
 *   The results are stored in Vx. See instruction 8xy2 for more information on ANDing values.
 */

class RNDVxByteCommand(private val random: Random) : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = (opcode and 0x00FFu).toUByte()

        val randomValue = random.nextInt(0, 256).toUByte()
        registerX.write(randomValue and value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is RNDVxByteCommand
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
