package com.tomassirio.emulation.chip8.controller.debug

import com.tomassirio.emulation.chip8.system.cpu.utils.SizedStack
import com.tomassirio.emulation.chip8.system.debug.DebugLevel
import com.tomassirio.emulation.chip8.system.io.keyboard.KeyboardState
import com.tomassirio.emulation.chip8.system.memory.Memory
import com.tomassirio.emulation.chip8.system.memory.accessor.MemoryAccessor
import com.tomassirio.emulation.chip8.system.register.Register
import com.tomassirio.emulation.chip8.system.register.utils.RegisterSet

class CPUDebugger(
    val memory: Memory,
    val registers: RegisterSet,
    val pc: Register.ShortRegister,
    val I: Register.ShortRegister,
    val DT: Register.TimerRegister,
    val ST: Register.TimerRegister,
    val keyboardState: KeyboardState,
    val stack: SizedStack<UShort>
) {
    var debugLevel: DebugLevel = DebugLevel.NONE
    private var cycleCount: Long = 0

    fun incrementCycle() {
        cycleCount++
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun logCycleStart(opcode: UShort) {
        when (debugLevel) {
            DebugLevel.FULL -> {
                debugStateDump()
                println("Cycle $cycleCount | PC: ${pc.read().toHexString()} | OpCode: ${opcode.toHexString()}")
            }
            DebugLevel.MINIMAL -> {
                println("Cycle $cycleCount | PC: ${pc.read().toHexString()} | OpCode: ${opcode.toHexString()}")
            }
            DebugLevel.NONE -> { /* No output */ }
        }
    }

    fun logWaitingForKey() {
        if (debugLevel != DebugLevel.NONE) {
            println("CPU waiting for key input...")
        }
    }

    fun logPostExecution(waitingForKey: Boolean) {
        if (debugLevel == DebugLevel.FULL) {
            debugPostExecutionState(waitingForKey)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun debugStateDump() {
        println("═══════════════════════════════════════")
        println("           CPU STATE DUMP")
        println("═══════════════════════════════════════")
        println("Cycle: $cycleCount")
        println("PC: ${pc.read().toHexString().padStart(4, '0')} (${pc.read()})")
        println("SP: ${stack.lastIndex} (Stack Depth: ${stack.size})")
        println("I:  ${I.read().toHexString().padStart(4, '0')} (${I.read()})")
        println("DT: ${DT.read()} | ST: ${ST.read()}")

        printRegistersTable()
        printStackTable()
        printMemoryAroundPC()
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun printRegistersTable() {
        println("\nRegisters:")
        println("┌─────┬──────┬───────┐")
        println("│ Reg │ Hex  │ Dec   │")
        println("├─────┼──────┼───────┤")
        registers.forEachIndexed { index, register ->
            val value = register.read().toUShort()
            println("│ V${index.toString(16).uppercase().padStart(1)}  │ ${value.toHexString().padStart(4, '0')} │ ${value.toString().padStart(5)} │")
        }
        println("└─────┴──────┴───────┘")
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun printStackTable() {
        if (stack.isNotEmpty()) {
            println("\nStack (top to bottom):")
            println("┌─────┬──────┬───────┐")
            println("│ Pos │ Hex  │ Dec   │")
            println("├─────┼──────┼───────┤")
            stack.reversed().forEachIndexed { index, value ->
                val pos = stack.lastIndex - index
                println("│ ${pos.toString().padStart(3)} │ ${value.toHexString().padStart(4, '0')} │ ${value.toString().padStart(5)} │")
            }
            println("└─────┴──────┴───────┘")
        } else {
            println("\nStack: Empty")
        }
    }

    @OptIn(ExperimentalStdlibApi::class, ExperimentalUnsignedTypes::class)
    private fun printMemoryAroundPC() {
        println("\nMemory around PC:")
        val pcVal = pc.read().toInt()
        for (offset in -4..4 step 2) {
            val addr = pcVal + offset
            if (addr >= 0 && addr < memory.size) {
                val value = MemoryAccessor.readUShort(memory, addr)
                val marker = if (offset == 0) ">>>" else "   "
                println("  $marker ${addr.toString(16).padStart(4, '0')}: ${value.toHexString().padStart(4, '0')}")
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun debugPostExecutionState(waitingForKey: Boolean) {
        println("---")
        println("After execution:")
        println("  New PC: ${pc.read().toHexString().padStart(4, '0')}")
        println("  Stack Depth: ${stack.size}")
        if (waitingForKey) {
            println("  Now waiting for key input")
        }
        println()
    }
}