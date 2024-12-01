package cpu.opcode

import com.tomassirio.cpu.opcode.commands.CLSCommand
import com.tomassirio.cpu.opcode.Command
import com.tomassirio.cpu.opcode.OpCodeTable
import com.tomassirio.cpu.opcode.commands.RETCommand
import com.tomassirio.cpu.opcode.commands.SYSAddrCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class OpcodeTableTest {
    @ParameterizedTest
    @MethodSource("opcodeChip8Provider")
    fun `test getCommand Chip8 returns the correct command`(
        opcode: Int,
        expectedCommand: Command) {
        val opcodeTable = OpCodeTable.chip8CommandGetter
        val command = opcodeTable(opcode.toUShort())
        assertThat(expectedCommand).isEqualTo(command)
    }

    companion object {
        @JvmStatic
        fun opcodeChip8Provider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0x00E0, CLSCommand),
                Arguments.of(0x00EE, RETCommand),
                Arguments.of(0x0000, SYSAddrCommand)
            )
        }
    }
}