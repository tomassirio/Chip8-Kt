package io.github.tomassirio.cli.params

import io.github.tomassirio.system.cpu.CPUType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.DefaultApplicationArguments

class CliParamsFactoryTest {

    @Test
    fun `fromArguments with only rom uses defaults for cpu and fps`() {
        val args = DefaultApplicationArguments("--rom=roms/games/PONG")

        val params = CliParamsFactory.fromArguments(args)

        assertThat(params.romPath).isEqualTo("roms/games/PONG")
        assertThat(params.cpuType).isEqualTo(CPUType.CHIP8)
        assertThat(params.fps).isEqualTo(15)
    }

    @Test
    fun `fromArguments reads cpu and fps when provided`() {
        val args = DefaultApplicationArguments("--rom=roms/games/PONG", "--cpu=SCHIP8", "--fps=30")

        val params = CliParamsFactory.fromArguments(args)

        assertThat(params.cpuType).isEqualTo(CPUType.SCHIP8)
        assertThat(params.fps).isEqualTo(30)
    }

    @Test
    fun `fromArguments without rom throws IllegalArgumentException`() {
        val args = DefaultApplicationArguments()

        assertThrows<IllegalArgumentException> {
            CliParamsFactory.fromArguments(args)
        }
    }

    @Test
    fun `fromArguments with unknown cpu type throws exception`() {
        val args = DefaultApplicationArguments("--rom=roms/games/PONG", "--cpu=NOPE")

        assertThrows<IllegalArgumentException> {
            CliParamsFactory.fromArguments(args)
        }
    }

    @Test
    fun `fromArguments with non-positive fps throws IllegalArgumentException`() {
        val args = DefaultApplicationArguments("--rom=roms/games/PONG", "--fps=0")

        assertThrows<IllegalArgumentException> {
            CliParamsFactory.fromArguments(args)
        }
    }
}
