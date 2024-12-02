package day_2;

import day_2.DayTwo.InputParser;
import day_2.DayTwo.Report;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayTwoTest {

    private static final List<String> INPUT = Arrays.asList(
            "7 6 4 2 1",
            "1 2 7 8 9",
            "9 7 6 2 1",
            "1 3 2 4 5",
            "8 6 4 4 1",
            "1 3 6 7 9"
    );

    @Test
    void should_parse_to_reports() {
        List<Report> reports = InputParser.parse(INPUT);
        assertThat(reports).map(Report::getLevels).containsExactly(
                Arrays.asList(7, 6, 4, 2, 1),
                Arrays.asList(1, 2, 7, 8, 9),
                Arrays.asList(9, 7, 6, 2, 1),
                Arrays.asList(1, 3, 2, 4, 5),
                Arrays.asList(8, 6, 4, 4, 1),
                Arrays.asList(1, 3, 6, 7, 9)
        );
    }

    @Test
    void should_validate_reports() {
        List<Report> reports = InputParser.parse(INPUT);
        assertThat(reports)
                .map(Report::isSafe)
                .containsExactly(true, false, false, false, false, true);
    }

    @Test
    void should_validate_reports_with_bad_level_tolerance() {
        List<Report> reports = InputParser.parse(INPUT);
        assertThat(reports)
                .map(Report::isSafeWithTolerance)
                .containsExactly(true, false, false, true, true, true);
    }
}