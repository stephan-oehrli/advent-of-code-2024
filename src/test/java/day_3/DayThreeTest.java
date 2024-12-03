package day_3;

import day_3.DayThree.InputParser;
import day_3.DayThree.MulOperation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayThreeTest {

    public static final String INPUT_1 = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
    public static final String INPUT_2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";

    @Test
    void should_parse_mul_operations() {
        assertThat(InputParser.parseMulOperations(List.of(INPUT_1)))
                .containsExactly(
                        new MulOperation(2, 4),
                        new MulOperation(5, 5),
                        new MulOperation(11, 8),
                        new MulOperation(8, 5)
                );
    }

    @Test
    void should_parse_conditional_mul_operations() {
        assertThat(InputParser.parseConditionalMulOperations(List.of(INPUT_2)))
                .containsExactly(
                        new MulOperation(2, 4),
                        new MulOperation(8, 5)
                );
    }

    @Test
    void should_calculate_product() {
        assertThat(
                InputParser.parseMulOperations(List.of(INPUT_1)).stream()
                        .map(MulOperation::calculateProduct)
                        .reduce(Integer::sum)
        ).hasValue(161);
    }

    @Test
    void should_calculate_conditional_product() {
        assertThat(
                InputParser.parseConditionalMulOperations(List.of(INPUT_2)).stream()
                        .map(MulOperation::calculateProduct)
                        .reduce(Integer::sum)
        ).hasValue(48);
    }
}