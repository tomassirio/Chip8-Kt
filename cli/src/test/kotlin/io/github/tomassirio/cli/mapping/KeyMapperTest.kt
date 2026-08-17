package io.github.tomassirio.cli.mapping

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KeyMapperTest {

    private val keyMapper = KeyMapper()

    @Test
    fun `maps physical keys to chip8 keys`() {
        assertThat(keyMapper.mapToChip8Key('1')).isEqualTo('1')
        assertThat(keyMapper.mapToChip8Key('4')).isEqualTo('C')
        assertThat(keyMapper.mapToChip8Key('q')).isEqualTo('4')
        assertThat(keyMapper.mapToChip8Key('r')).isEqualTo('D')
        assertThat(keyMapper.mapToChip8Key('z')).isEqualTo('A')
        assertThat(keyMapper.mapToChip8Key('v')).isEqualTo('F')
    }

    @Test
    fun `mapping is case insensitive`() {
        assertThat(keyMapper.mapToChip8Key('Q')).isEqualTo('4')
    }

    @Test
    fun `unmapped key returns null`() {
        assertThat(keyMapper.mapToChip8Key('k')).isNull()
    }
}
