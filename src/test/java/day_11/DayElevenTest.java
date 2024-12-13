package day_11;

import day_11.DayEleven.InputParser;
import day_11.DayEleven.StoneLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DayElevenTest {

    public static final String INPUT = "125 17";

    @Test
    void should_parse_stone_list() {
        StoneLine stoneLine = InputParser.parse(INPUT);
        assertThat(stoneLine.getStones()).containsExactly(125L, 17L);
    }

    @Test
    void should_blink_twice_correctly() {
        StoneLine stoneLine = InputParser.parse(INPUT);
        assertThat(stoneLine.countStones(25)).isEqualTo(55312);
    }
}