package cpu.utils

import com.tomassirio.cpu.Register
import com.tomassirio.cpu.exception.RegisterNotFoundException
import com.tomassirio.cpu.utils.RegisterSet
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class RegisterSetTest {

    @Test
    fun `should retrieve a register by name`() {
        val registerSet = RegisterSet.Builder<Register.ByteRegister>()
            .addRegister("0", 0x1u)
            .addRegister("1", 0x2u)
            .addRegister("2", 0x3u)
            .build()

        val v0 = registerSet["0"]
        val v1 = registerSet["1"]

        assertThat(v0.read()).isEqualTo(0x1u.toUByte())
        assertThat(v1.read()).isEqualTo(0x2u.toUByte())
    }

    @Test
    fun `should throw exception when retrieving a non-existent register`() {
        val registerSet = RegisterSet.Builder<Register.ByteRegister>()
            .addRegister("v0", 0x1u)
            .build()

        assertThatThrownBy {
            registerSet["v1"]
        }.isInstanceOf(RegisterNotFoundException::class.java)
    }

    @Test
    fun `should support iteration over registers`() {
        val registerSet = RegisterSet.Builder<Register.ByteRegister>()
            .addRegister("v0", 0x1u)
            .addRegister("v1", 0x2u)
            .addRegister("v2", 0x3u)
            .build()

        val registerNames = registerSet.map { it.name }
        assertThat("v0").isIn(registerNames)
        assertThat("v1").isIn(registerNames)
        assertThat("v2").isIn(registerNames)
    }

    @Test
    fun `should build an empty register set`() {
        val registerSet = RegisterSet.Builder<Register.ByteRegister>().build()

        assertThatThrownBy { registerSet["v0"] }
            .isInstanceOf(RegisterNotFoundException::class.java)

        assertThat(registerSet.iterator().hasNext()).isFalse()
    }

    @Test
    fun `should allow multiple registers with distinct names`() {
        val registerSet = RegisterSet.Builder<Register.ByteRegister>()
            .addRegister("v0", 0x1u)
            .addRegister("v1", 0x2u)
            .addRegister("vF", 0xFFu)
            .build()

        val v0 = registerSet["v0"]
        val v1 = registerSet["v1"]
        val vF = registerSet["vF"]

        assertThat(v0.read()).isEqualTo(0x1u.toUByte())
        assertThat(v1.read()).isEqualTo(0x2u.toUByte())
        assertThat(vF.read()).isEqualTo(0xFFu.toUByte())
    }
}
