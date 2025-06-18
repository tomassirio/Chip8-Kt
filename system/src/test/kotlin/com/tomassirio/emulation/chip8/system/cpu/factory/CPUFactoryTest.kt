package com.tomassirio.emulation.chip8.system.cpu.factory

import com.tomassirio.emulation.chip8.system.cpu.CPU
import com.tomassirio.emulation.chip8.system.cpu.CPUType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CPUFactoryTest {

    private val cpuFactory: CPUFactory = CPUFactory

    @Test
    fun `createCPU with CHIP8 type returns initialized CHIP8 CPU`() {
        val chip8: CPU = cpuFactory.createCPU(CPUType.CHIP8)

        // Assert that CPU is initialized
        assertThat(chip8).isNotNull

        // Check memory is initialized
        assertThat(chip8.memory).isNotNull

        // Check registers are initialized and PC starts at 0x200
        assertThat(chip8.pc.read()).isEqualTo(0x200u.toUShort())
        assertThat(chip8.stack.lastIndex).isEqualTo(-1)
        assertThat(chip8.I.read()).isEqualTo(0x0u.toUShort())

        // Check that the register set is initialized correctly
        assertThat(chip8.registers).hasSize(16)
        assertThat(chip8.registers.map { it.read() }).containsOnly(0x0u)

        // Check that the stack is initialized to 16 empty entries
        assertThat(chip8.stack.maxSize).isEqualTo(16)
        assertThat(chip8.stack).isEmpty()

        // Check that keyboard and display are initialized
        assertThat(chip8.keyboardState).isNotNull
        assertThat(chip8.displayState).isNotNull
    }

    @Test
    fun `createCPU with SuperChip8 type returns initialized SuperChip8 CPU`() {
        val superChip8: CPU = cpuFactory.createCPU(CPUType.SCHIP8)

        // Assert that CPU is initialized
        assertThat(superChip8).isNotNull

        // Check memory is initialized (specific memory for SuperChip8)
        assertThat(superChip8.memory).isNotNull

        // Check PC, SP, and I registers
        assertThat(superChip8.pc.read()).isEqualTo(0x200u.toUShort())
        assertThat(superChip8.stack.lastIndex).isEqualTo(-1)
        assertThat(superChip8.I.read()).isEqualTo(0x0u.toUShort())

        // Check that the register set is initialized correctly
        assertThat(superChip8.registers).hasSize(16)

        // Check that the stack is initialized to 16 empty entries
        assertThat(superChip8.stack.maxSize).isEqualTo(16)
        assertThat(superChip8.stack).isEmpty()

        // Check that keyboard and display are initialized
        assertThat(superChip8.keyboardState).isNotNull
        assertThat(superChip8.displayState).isNotNull
    }
}