package day_7;

import day_7.DaySeven.InputParser;
import day_7.DaySeven.Operation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class DaySevenTest {

    public static final List<String> INPUT = asList(
            "190: 10 19",
            "3267: 81 40 27",
            "83: 17 5",
            "156: 15 6",
            "7290: 6 8 6 15",
            "161011: 16 10 13",
            "192: 17 8 14",
            "21037: 9 7 18 13",
            "292: 11 6 16 20"
    );

    @Test
    void should_parse_operations() {
        List<Operation> operations = InputParser.parse(INPUT);
        assertThat(operations)
                .containsExactly(
                        Operation.of(190L, asList(10, 19)),
                        Operation.of(3267L, asList(81, 40, 27)),
                        Operation.of(83L, asList(17, 5)),
                        Operation.of(156L, asList(15, 6)),
                        Operation.of(7290L, asList(6, 8, 6, 15)),
                        Operation.of(161011L, asList(16, 10, 13)),
                        Operation.of(192L, asList(17, 8, 14)),
                        Operation.of(21037L, asList(9, 7, 18, 13)),
                        Operation.of(292L, asList(11, 6, 16, 20))
                );
    }

    @Test
    void should_calculate_valid_equation_sum_with_2_different_operators() {
        List<Operation> operations = InputParser.parse(INPUT);
        assertThat(operations.stream()
                .filter(Operation::isValidEquation)
                .map(Operation::getResult)
                .reduce(Long::sum)
        ).hasValue(3749L);
    }

    @Test
    void should_calculate_valid_equation_sum_with_3_different_operators() {
        List<Operation> operations = InputParser.parse(INPUT);
        assertThat(operations.stream()
                .filter(Operation::isValidEquationWithConcatenation)
                .map(Operation::getResult)
                .reduce(Long::sum)
        ).hasValue(11387L);
    }
}