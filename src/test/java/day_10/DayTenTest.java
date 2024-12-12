package day_10;

import day_10.DayTen.InputParser;
import day_10.DayTen.Position;
import day_10.DayTen.TopographicMap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayTenTest {

    public static final List<String> SIMPLE_INPUT = Arrays.asList(
            "10..9..",
            "2...8..",
            "3...7..",
            "4567654",
            "...8..3",
            "...9..2",
            ".....01"
    );

    public static final List<String> ANOTHER_SIMPLE_INPUT = Arrays.asList(
            "..90..9",
            "...1.98",
            "...2..7",
            "6543456",
            "765.987",
            "876....",
            "987...."
    );

    public static final List<String> INPUT = Arrays.asList(
            "89010123",
            "78121874",
            "87430965",
            "96549874",
            "45678903",
            "32019012",
            "01329801",
            "10456732"
    );

    @Test
    void should_parse_topographic_map() {
        TopographicMap topographicMap = InputParser.parse(SIMPLE_INPUT);
        assertThat(topographicMap.trailHeads()).containsExactly(Position.of(1, 0, 0), Position.of(5, 6, 0));
    }

    @Test
    void should_find_simple_input_trails() {
        TopographicMap topographicMap = InputParser.parse(SIMPLE_INPUT);
        assertThat(topographicMap.findTrails()).isEqualTo(3);
    }

    @Test
    void should_find_another_simple_input_trails() {
        TopographicMap topographicMap = InputParser.parse(ANOTHER_SIMPLE_INPUT);
        assertThat(topographicMap.findTrails()).isEqualTo(4);
    }

    @Test
    void should_find_input_trails() {
        TopographicMap topographicMap = InputParser.parse(INPUT);
        assertThat(topographicMap.findTrails()).isEqualTo(36);
    }

    @Test
    void should_find_input_ratings() {
        TopographicMap topographicMap = InputParser.parse(INPUT);
        assertThat(topographicMap.findRatings()).isEqualTo(81);
    }
}