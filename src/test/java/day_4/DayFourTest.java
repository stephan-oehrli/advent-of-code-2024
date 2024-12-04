package day_4;

import day_4.DayFour.InputParser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayFourTest {

    private static final List<String> INPUT = Arrays.asList(
            "MMMSXXMASM",
            "MSAMXMSMSA",
            "AMXSXMAAMM",
            "MSAMASMSMX",
            "XMASAMXAMM",
            "XXAMMXXAMA",
            "SMSMSASXSS",
            "SAXAMASAAA",
            "MAMMMXMMMM",
            "MXMXAXMASX"
    );

    @Test
    void should_parse_word_search() {
        assertThat(InputParser.parse(INPUT).lines())
                .containsExactly(
                        "MMMSXXMASM", "MSAMXMSMSA", "AMXSXMAAMM", "MSAMASMSMX", "XMASAMXAMM", "XXAMMXXAMA", "SMSMSASXSS", "SAXAMASAAA", "MAMMMXMMMM", "MXMXAXMASX",
                        "MMAMXXSSMM", "MSMSMXMAAX", "MAXAAASXMM", "SMSMSMMAMX", "XXXAAMSMMA", "XMMSMXAAXX", "MSAMXXSSMM", "AMASAAXAMA", "SSMMMMSAMS", "MAMXMASAMX",
                        "M", "MM", "MSA", "SAMM", "XMXSX", "XXSAMX", "MMXMAXS", "ASMASAMS", "SMASAMSAM", "MSAMMMMXAM",
                        "AMSXXSAMX", "MMAXAMMM", "XMASAMX", "MMXSXA", "ASAMX", "SAMM", "AMA", "MS", "X",
                        "M", "SA", "ASM", "MMMX", "XSAMM", "XMASMA", "SXMMAMS", "MMXSXASA", "MASAMXXAM", "MSXMAXSAMX",
                        "MMASMASMS", "ASAMSAMA", "MMAMMXM", "XXSAMX", "XMXMA", "SAMX", "SAM", "MX", "M"
                );
    }

    @Test
    void should_count_xmas() {
        assertThat(InputParser.parse(INPUT).countXmas()).isEqualTo(18);
    }

    @Test
    void should_count_cross_mas() {
        assertThat(InputParser.parse(INPUT).countCrossMas()).isEqualTo(9);
    }
}