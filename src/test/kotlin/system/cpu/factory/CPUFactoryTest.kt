package system.cpu.factory

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.CPUType
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
    fun `createCPU with ETI660 type returns initialized ETI660 CPU`() {
        val eti660: CPU = cpuFactory.createCPU(CPUType.ETI660)

        // Assert that CPU is initialized
        assertThat(eti660).isNotNull

        // Check memory is initialized (specific memory for ETI660)
        assertThat(eti660.memory).isNotNull

        // Check PC, SP, and I registers
        assertThat(eti660.pc.read()).isEqualTo(0x600u.toUShort())
        assertThat(eti660.stack.lastIndex).isEqualTo(-1)
        assertThat(eti660.I.read()).isEqualTo(0x0u.toUShort())

        // Check that the register set is initialized correctly
        assertThat(eti660.registers).hasSize(16)

        // Check that the stack is initialized to 16 empty entries
        assertThat(eti660.stack.maxSize).isEqualTo(16)
        assertThat(eti660.stack).isEmpty()

        // Check that keyboard and display are initialized
        assertThat(eti660.keyboardState).isNotNull
        assertThat(eti660.displayState).isNotNull
    }
}