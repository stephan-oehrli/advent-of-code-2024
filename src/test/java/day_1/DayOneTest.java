package day_1;

import day_1.DayOne.InputParser;
import day_1.DayOne.LocationLists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayOneTest {

    private static final List<String> INPUT = Arrays.asList(
            "3   4",
            "4   3",
            "2   5",
            "1   3",
            "3   9",
            "3   3"
    );

    @Test
    void should_parse_location_lists() {
        LocationLists locationLists = InputParser.parse(INPUT);

        assertThat(locationLists.leftList()).containsExactly(3, 4, 2, 1, 3, 3);
        assertThat(locationLists.rightList()).containsExactly(4, 3, 5, 3, 9, 3);
    }

    @Test
    void should_calculate_distance() {
        assertThat(InputParser.parse(INPUT).calculateDistance()).isEqualTo(11);
    }

    @Test
    void should_calculate_similarity_score() {
        assertThat(InputParser.parse(INPUT).calculateSimilarityScore()).isEqualTo(31);
    }
}