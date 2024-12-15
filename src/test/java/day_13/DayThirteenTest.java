package day_13;

import day_13.DayThirteen.Calculator;
import day_13.DayThirteen.ClawMachine;
import day_13.DayThirteen.InputParser;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayThirteenTest {

    public static final List<String> INPUT = Arrays.asList(
            "Button A: X+94, Y+34",
            "Button B: X+22, Y+67",
            "Prize: X=8400, Y=5400",
            "",
            "Button A: X+26, Y+66",
            "Button B: X+67, Y+21",
            "Prize: X=12748, Y=12176",
            "",
            "Button A: X+17, Y+86",
            "Button B: X+84, Y+37",
            "Prize: X=7870, Y=6450",
            "",
            "Button A: X+69, Y+23",
            "Button B: X+27, Y+71",
            "Prize: X=18641, Y=10279"
    );

    @Test
    void should_parse_claw_machines() {
        List<ClawMachine> clawMachines = InputParser.parseWithNormalPrize(INPUT);
        assertThat(clawMachines).hasSize(4);
        assertThat(clawMachines.get(2))
                .extracting(ClawMachine::buttonA, ClawMachine::buttonB, ClawMachine::prize)
                .containsExactly(new Point(17, 86), new Point(84, 37), new Point(7870, 6450));
    }

    @Test
    void should_calculate_fewest_tokens_normal_prize() {
        List<ClawMachine> clawMachines = InputParser.parseWithNormalPrize(INPUT);
        assertThat(Calculator.calculateFewestTokensToWin(clawMachines)).isEqualTo(480);
    }

    @Test
    void should_calculate_fewest_tokens_increased_prize() {
        List<ClawMachine> clawMachines = InputParser.parseWithIncreasedPrize(INPUT);
        assertThat(Calculator.calculateFewestTokensToWin(clawMachines)).isEqualTo(875318608908L);
    }
}