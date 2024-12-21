package day_14;

import day_14.DayFourteen.Bathroom;
import day_14.DayFourteen.Robot;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayFourteenTest {

    public static final List<String> INPUT = Arrays.asList(
            "p=0,4 v=3,-3",
            "p=6,3 v=-1,-3",
            "p=10,3 v=-1,2",
            "p=2,0 v=2,-1",
            "p=0,0 v=1,3",
            "p=3,0 v=-2,-2",
            "p=7,6 v=-1,-3",
            "p=3,0 v=-1,-2",
            "p=9,3 v=2,3",
            "p=7,3 v=-1,2",
            "p=2,4 v=2,-3",
            "p=9,5 v=-3,-3"
    );

    @Test
    void should_parse_robots() {
        assertThat(Bathroom.of(INPUT, 11, 7).getRobots())
                .hasSize(12)
                .element(2)
                .extracting(Robot::position, Robot::velocity)
                .containsExactly(new Point(10, 3), new Point(-1, 2));
    }

    @Test
    void should_calculate_safety_factor() {
        Bathroom bathroom = Bathroom.of(INPUT, 11, 7);
        assertThat(bathroom.calculateSafetyFactor(100)).isEqualTo(12);
    }

    @Test
    void should_move_correctly() {
        Bathroom bathroom = Bathroom.of(INPUT, 11, 7);
        Robot robot = new Robot(new Point(4, 1), new Point(2, -3), bathroom);
        robot.move(1);
        assertThat(robot)
                .extracting(Robot::positionX, Robot::positionY)
                .containsExactly(6, 5);
    }
}