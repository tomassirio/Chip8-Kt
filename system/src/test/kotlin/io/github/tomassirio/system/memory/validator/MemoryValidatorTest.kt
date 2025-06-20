package io.github.tomassirio.system.memory.validator

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class MemoryValidatorTest {

    private lateinit var memoryValidator: MemoryValidator

    @Test
    fun `validate address should throw OutOfBoundsWhen not in memory range`() {
        memoryValidator = MemoryValidator(
            memorySize = 2048,
            startAddress = 200,
            isLocked = { false }
        )
        assertThatThrownBy { memoryValidator.validate(2049) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `validateWrite cant write to locked section after locking memory`() {
        memoryValidator = MemoryValidator(
            memorySize = 2048,
            startAddress = 200,
            isLocked = { true }
        )
        assertThatThrownBy {memoryValidator.validateWrite(0)}
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `validateWrite cant write to out of bounds address`() {
        memoryValidator = MemoryValidator(
            memorySize = 2048,
            startAddress = 200,
            isLocked = { true }
        )
        assertThatThrownBy {memoryValidator.validateWrite(2049)}
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}