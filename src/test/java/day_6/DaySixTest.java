package day_6;

import day_6.DaySix.InputParser;
import day_6.DaySix.ManufacturingLab;
import day_6.DaySix.Position;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class DaySixTest {

    private static final List<String> INPUT = Arrays.asList(
            "....#.....",
            ".........#",
            "..........",
            "..#.......",
            ".......#..",
            "..........",
            ".#..^.....",
            "........#.",
            "#.........",
            "......#..."
    );

    @Test
    void should_parse_position_map() {
        List<List<Position>> positionMap = InputParser.parse(INPUT).getGuard().getPositionMap();
        assertThat(positionMap).flatMap(l -> l.stream().filter(Position::isObstruction).toList())
                .containsExactly(
                        new Position(4, 0, true),
                        new Position(9, 1, true),
                        new Position(2, 3, true),
                        new Position(7, 4, true),
                        new Position(1, 6, true),
                        new Position(8, 7, true),
                        new Position(0, 8, true),
                        new Position(6, 9, true)
                );
    }

    @Test
    void should_count_distinct_guardian_positions() {
        ManufacturingLab manufacturingLab = InputParser.parse(INPUT);
        assertThat(manufacturingLab.countDistinctGuardianPositions()).isEqualTo(41);
    }

    @Test
    void should_count_possible_obstruction_positions() {
        ManufacturingLab manufacturingLab = InputParser.parse(INPUT);
        assertThat(manufacturingLab.countPossibleObstructionPositions()).isEqualTo(6);
        assertThat(manufacturingLab.getPossibleObstructions())
                .map(Position::getX, Position::getY)
                .containsExactlyInAnyOrder(
                        tuple(3, 6),
                        tuple(6, 7),
                        tuple(7, 7),
                        tuple(1, 8),
                        tuple(3, 8),
                        tuple(7, 9)
                );
    }
}