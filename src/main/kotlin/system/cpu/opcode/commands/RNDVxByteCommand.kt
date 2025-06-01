package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command
import kotlin.random.Random

/**
 *   Cxkk - RND Vx, byte
 *   Set Vx = random byte AND kk.
 *
 *   The interpreter generates a random number from 0 to 255, which is then ANDed with the value kk.
 *   The results are stored in Vx. See instruction 8xy2 for more information on ANDing values.
 */
fun rndVxByteCommand(random: Random = Random.Default): Command {
    return CommandWrapper(commandType = "RNDVxByteCommand") { cpu, opcode ->
        val registerX = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val value = (opcode and 0x00FFu).toUByte()

        val randomValue = random.nextInt(0, 256).toUByte()
        registerX.write(randomValue and value)
    }
}

class CommandWrapper(
    skipsPcIncrement: Boolean = false,
    private val commandType: String,
    private val executeFunc: (CPU, UShort) -> Unit
) : Command(skipsPcIncrement, executeFunc) {

    fun execute(cpu: CPU, opcode: UShort) {
        executeFunc(cpu, opcode)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is CommandWrapper && commandType == other.commandType
    }

    override fun hashCode(): Int {
        return commandType.hashCode()
    }
}