package system.cpu.opcode.commands

import com.tomassirio.system.cpu.CPU
import com.tomassirio.system.cpu.factory.CPUFactory
import com.tomassirio.system.cpu.opcode.commands.ldVxKCommand
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LDVxKCommandTest {
  private val command = ldVxKCommand()
    private lateinit var cpu: CPU

    @BeforeEach
    fun setup() {
        cpu = CPUFactory.createCPU()
    }

    @Test
    fun `LDVxKCommand should mark the CPU with the register to write the key to`() {
        // Given
        val opcode: UShort = 0xF00Au.toUShort()

        // When
        command.execute(cpu, opcode)

        // Then
        assertThat(cpu.keyboardState.isWaitingForKey).isTrue()
        assertThat(cpu.keyboardState.registerToStoreKeyIn).isEqualTo(0)
    }
}