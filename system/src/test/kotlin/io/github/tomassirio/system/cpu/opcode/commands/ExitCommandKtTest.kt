package io.github.tomassirio.system.cpu.opcode.commands

import io.github.tomassirio.system.cpu.CPU
import io.github.tomassirio.system.cpu.CPUType
import io.github.tomassirio.system.cpu.factory.CPUFactory
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