package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.CPUType
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.exitCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExitCommandKtTest {
    private val command = exitCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU(CPUType.SCHIP8)
    }

    @Test
    fun testExitCommand() {
        command.execute(cpu, 0x00FDu)

        assertThat(cpu.halted).isTrue()
    }

}