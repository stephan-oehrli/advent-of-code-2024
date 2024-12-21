package day_15;

import day_15.DayFifteen.InputParser;
import day_15.DayFifteen.Position;
import day_15.DayFifteen.Warehouse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayFifteenTest {

    public static final List<String> SMALL_INPUT = Arrays.asList(
            "########",
            "#..O.O.#",
            "##@.O..#",
            "#...O..#",
            "#.#.O..#",
            "#...O..#",
            "#......#",
            "########",
            "",
            "<^^>>>vv",
            "<v>>v<<"
    );

    public static final List<String> ANOTHER_SMALL_INPUT = Arrays.asList(
            "#######",
            "#...#.#",
            "#.....#",
            "#..OO@#",
            "#..O..#",
            "#.....#",
            "#######",
            "",
            "<vv<<^^<<^^"
    );

    public static final List<String> LARGE_INPUT = Arrays.asList(
            "##########",
            "#..O..O.O#",
            "#......O.#",
            "#.OO..O.O#",
            "#..O@..O.#",
            "#O#..O...#",
            "#O..O..O.#",
            "#.OO.O.OO#",
            "#....O...#",
            "##########",
            "",
            "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^",
            "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v",
            "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<",
            "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^",
            "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><",
            "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^",
            ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^",
            "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>",
            "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>",
            "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^"
    );

    @Test
    void should_parse_warehouse() {
        Warehouse smallWarehouse = InputParser.parseSmallWarehouse(SMALL_INPUT);
        assertThat(smallWarehouse.getPositions()).hasSize(8);
        assertThat(smallWarehouse.getMovements()).isEqualTo("<^^>>>vv<v>>v<<");
        assertThat(smallWarehouse.getRobotPosition())
                .extracting(Position::getX, Position::getY)
                .containsExactly(2, 2);
    }

    @Test
    void should_move_boxes() {
        Warehouse smallWarehouse = InputParser.parseSmallWarehouse(SMALL_INPUT);
        smallWarehouse.moveBoxes();
        assertThat(smallWarehouse.getPositions().get(1))
                .extracting(Position::getSymbol)
                .containsExactly('#', '.', '.', '.', '.', 'O', 'O', '#');
    }

    @Test
    void should_calculate_box_coordinates_sum_of_small_input() {
        Warehouse smallWarehouse = InputParser.parseSmallWarehouse(SMALL_INPUT);
        smallWarehouse.moveBoxes();
        assertThat(smallWarehouse.calculateSumOfBoxCoordinates()).isEqualTo(2028);
    }

    @Test
    void should_calculate_box_coordinates_sum_of_large_input() {
        Warehouse smallWarehouse = InputParser.parseSmallWarehouse(LARGE_INPUT);
        smallWarehouse.moveBoxes();
        assertThat(smallWarehouse.calculateSumOfBoxCoordinates()).isEqualTo(10092);
    }

    @Test
    void should_calculate_big_warehouse_box_coordinates_sum_of_small_input() {
        Warehouse bigWarehouse = InputParser.parseBigWarehouse(ANOTHER_SMALL_INPUT);
        bigWarehouse.moveBoxes();
        assertThat(bigWarehouse.calculateSumOfBoxCoordinates()).isEqualTo(618);
    }

    @Test
    void should_calculate_big_warehouse_box_coordinates_sum_of_large_input() {
        Warehouse bigWarehouse = InputParser.parseBigWarehouse(LARGE_INPUT);
        bigWarehouse.moveBoxes();
        assertThat(bigWarehouse.calculateSumOfBoxCoordinates()).isEqualTo(9021);
    }
}