package utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ParserUtilTest {

    @Test
    void should_split_by_whitespace() {
        assertThat(ParserUtil.splitByWhiteSpace("ab  c de "))
                .containsExactly("ab", "c", "de");
    }

    @Test
    void should_convert_to_integer_list() {
        assertThat(ParserUtil.convertToIntegerList(Arrays.asList("1", "0", "4", "7")))
                .containsExactly(1, 0, 4, 7);
    }

}