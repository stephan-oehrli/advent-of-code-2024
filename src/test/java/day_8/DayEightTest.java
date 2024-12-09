package day_8;

import day_8.DayEight.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class DayEightTest {

    public static final List<String> INPUT = Arrays.asList(
            "............",
            "........0...",
            ".....0......",
            ".......0....",
            "....0.......",
            "......A.....",
            "............",
            "............",
            "........A...",
            ".........A..",
            "............",
            "............"
    );

    @Test
    void should_parse_frequency_map() {
        Map<Character, List<Frequency>> frequencyMap = InputParser.parse(INPUT).frequencyMap();
        assertThat(frequencyMap.get('0'))
                .extracting(Frequency::x, Frequency::y)
                .containsExactly(
                        tuple(8, 1),
                        tuple(5, 2),
                        tuple(7, 3),
                        tuple(4, 4)
                );
        assertThat(frequencyMap.get('A'))
                .extracting(Frequency::x, Frequency::y)
                .containsExactly(
                        tuple(6, 5),
                        tuple(8, 8),
                        tuple(9, 9)
                );
    }

    @Test
    void should_calculate_antinode() {
        Frequency frequency1 = new Frequency(4, 3, new HashSet<>(), new MapDimensions(12, 12));
        Frequency frequency2 = new Frequency(5, 5, new HashSet<>(), new MapDimensions(12, 12));
        frequency1.calculateAntinode(frequency2);
        assertThat(frequency1.antinodes()).containsOnly(new Antinode(3, 1));
        assertThat(frequency2.antinodes()).containsOnly(new Antinode(6, 7));
    }

    @Test
    void should_calculate_all_visible_antinodes() {
        FrequencyCalculator calculator = InputParser.parse(INPUT);
        assertThat(calculator.calculateAllVisibleAntinodes()).isEqualTo(14);
    }

    @Test
    void should_calculate_all_visible_antinodes_with_updated_model() {
        FrequencyCalculator calculator = InputParser.parse(INPUT);
        assertThat(calculator.calculateAllVisibleAntinodesWithUpdatedModel()).isEqualTo(34);
    }
}