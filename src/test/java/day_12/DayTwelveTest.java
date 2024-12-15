package day_12;

import day_12.DayTwelve.Garden;
import day_12.DayTwelve.InputParser;
import day_12.DayTwelve.Position;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static day_12.DayTwelve.Garden.BY_PERIMETER;
import static day_12.DayTwelve.Garden.BY_REGION_SIDES;
import static org.assertj.core.api.Assertions.assertThat;

class DayTwelveTest {

    public static final List<String> INPUT_1 = Arrays.asList(
            "RRRRIICCFF",
            "RRRRIICCCF",
            "VVRRRCCFFF",
            "VVRCCCJFFF",
            "VVVVCJJCFE",
            "VVIVCCJJEE",
            "VVIIICJJEE",
            "MIIIIIJJEE",
            "MIIISIJEEE",
            "MMMISSJEEE"
    );

    public static final List<String> INPUT_2 = Arrays.asList(
            "OOOOO",
            "OXOXO",
            "OOOOO",
            "OXOXO",
            "OOOOO"
    );

    @Test
    void should_parse_map_of_input_1() {
        List<List<Position>> map = InputParser.parse(INPUT_1).getMap();
        assertThat(map.get(1).get(1)).isEqualTo(Position.of(1, 1, 'R'));
        assertThat(map.get(4).get(4)).isEqualTo(Position.of(4, 4, 'C'));
        assertThat(map.get(9).get(9)).isEqualTo(Position.of(9, 9, 'E'));
    }

    @Test
    void should_group_map_of_input_1() {
        Garden garden = InputParser.parse(INPUT_1);
        List<List<Position>> groups = garden.getGroups();
        assertThat(groups).hasSize(11);
        assertThat(groups.get(0).get(0).getPlant()).isEqualTo('R');
    }

    @Test
    void should_group_map_of_input_2() {
        Garden garden = InputParser.parse(INPUT_2);
        List<List<Position>> groups = garden.getGroups();
        assertThat(groups).hasSize(5);
        assertThat(groups.get(0).get(0).getPlant()).isEqualTo('O');
        assertThat(IntStream.range(1, 4)
                .mapToObj(i -> groups.get(i).get(0).getPlant()).toList())
                .containsOnly('X');
    }

    @Test
    void should_calculate_total_price_by_perimeter_for_input_1() {
        Garden garden = InputParser.parse(INPUT_1);
        assertThat(garden.calculateTotalPrice(BY_PERIMETER)).isEqualTo(1930);
    }

    @Test
    void should_calculate_total_price_by_region_sides_for_input_2() {
        Garden garden = InputParser.parse(INPUT_2);
        assertThat(garden.calculateTotalPrice(BY_REGION_SIDES)).isEqualTo(436);
    }
}