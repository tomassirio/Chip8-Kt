package utils

import com.tomassirio.utils.SizedStack
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SizedStackTest {

    @Test
    fun `test push adds an element to the stack`() {
        val stack = SizedStack<Int>(3)
        stack.push(1)
        assertThat(stack.size).isEqualTo(1)
    }

    @Test
    fun `test push removes the first element when the stack is full`() {
        val stack = SizedStack<Int>(3)
        stack.push(1)
        stack.push(2)
        stack.push(3)
        stack.push(4)
        assertThat(stack.size).isEqualTo(3)
        assertThat(stack[0]).isEqualTo(2)
    }
}