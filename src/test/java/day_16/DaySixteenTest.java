package day_16;

import day_16.DaySixteen.InputParser;
import day_16.DaySixteen.PathFinder;
import day_16.DaySixteen.Position;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DaySixteenTest {

    private static final List<String> INPUT = Arrays.asList(
            "###############",
            "#.......#....E#",
            "#.#.###.#.###.#",
            "#.....#.#...#.#",
            "#.###.#####.#.#",
            "#.#.#.......#.#",
            "#.#.#####.###.#",
            "#...........#.#",
            "###.#.#####.#.#",
            "#...#.....#.#.#",
            "#.#.#.###.#.#.#",
            "#.....#...#.#.#",
            "#.###.#.#.#.#.#",
            "#S..#.....#...#",
            "###############"
    );

    public static final List<String> INPUT_2 = Arrays.asList(
            "#################",
            "#...#...#...#..E#",
            "#.#.#.#.#.#.#.#.#",
            "#.#.#.#...#...#.#",
            "#.#.#.#.###.#.#.#",
            "#...#.#.#.....#.#",
            "#.#.#.#.#.#####.#",
            "#.#...#.#.#.....#",
            "#.#.#####.#.###.#",
            "#.#.#.......#...#",
            "#.#.###.#####.###",
            "#.#.#...#.....#.#",
            "#.#.#.#####.###.#",
            "#.#.#.........#.#",
            "#.#.#.#########.#",
            "#S#.............#",
            "#################"
    );

    @Test
    void should_parse_path_finder() {
        PathFinder pathFinder = InputParser.parse(INPUT);
        assertThat(pathFinder.getMazeMap()).hasSize(13);
        assertThat(pathFinder.getStart()).isEqualTo(Position.of(0, 12, 'S'));
        assertThat(pathFinder.getEnd()).isEqualTo(Position.of(12, 0, 'E'));
    }

    @Test
    void should_find_best_path_score() {
        PathFinder pathFinder = InputParser.parse(INPUT);
        assertThat(pathFinder.findBestPathScore()).isEqualTo(7036);
    }

    @Test
    void should_find_another_best_path_score() {
        PathFinder pathFinder = InputParser.parse(INPUT_2);
        assertThat(pathFinder.findBestPathScore()).isEqualTo(11048);
    }

    @Test
    void should_find_number_of_best_path_tiles() {
        PathFinder pathFinder = InputParser.parse(INPUT);
        assertThat(pathFinder.findNumberOfBestPathTiles()).isEqualTo(45);
    }

    @Test
    void should_find_another_number_of_best_path_tiles() {
        PathFinder pathFinder = InputParser.parse(INPUT_2);
        assertThat(pathFinder.findNumberOfBestPathTiles()).isEqualTo(64);
    }
}