package com.tomassirio.system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.opcode.Command

/**
 * 8xy6 - SHR Vx {, Vy}
 * Set Vx = Vx SHR 1.
 *
 * If the least significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2.
*/

object SHRVxCommand : Command {
    override fun execute(cpu: CPU, opcode: UShort) {
        val vxRegister = cpu.registers[(opcode and 0xF00u).toInt() shr 8]
        val vxValue = vxRegister.read()

        cpu.registers[0xF].write(if(vxValue.toInt() and 0x1 == 1) 1u else 0u)
        vxRegister.write(vxValue.toInt().shr(1).toUByte())
    }
}
