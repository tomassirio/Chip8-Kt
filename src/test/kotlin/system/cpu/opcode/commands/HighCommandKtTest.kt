package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.CPUType
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.highCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HighCommandKtTest {
    private val command = highCommand()
    private lateinit var cpu: CPU

    @Test
    fun testHighCommand() {
        // Given
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
        val opcode: UShort = 0x00FFu // B123

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.displayState.isExtended()).isTrue()
    }
}