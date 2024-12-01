package cpu.opcode

import com.tomassirio.cpu.opcode.CLSCommand
import com.tomassirio.cpu.opcode.Command
import com.tomassirio.cpu.opcode.OpcodeTable
import com.tomassirio.cpu.opcode.RETCommand
import com.tomassirio.cpu.opcode.SYSAddrCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class OpcodeTableTest {
    @ParameterizedTest
    @MethodSource("opcodeProvider")
    fun `test getCommand returns the correct command`(
        opcode: Int,
        expectedCommand: Command) {
        val opcodeTable = OpcodeTable()
        val command = opcodeTable.getCommand(opcode.toUShort())
        assertThat(expectedCommand).isEqualTo(command)
    }

    companion object {
        @JvmStatic
        fun opcodeProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0x00E0, CLSCommand),
                Arguments.of(0x00EE, RETCommand),
                Arguments.of(0x0000, SYSAddrCommand)
            )
        }
    }
}