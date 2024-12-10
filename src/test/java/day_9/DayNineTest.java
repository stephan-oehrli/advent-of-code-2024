package day_9;

import day_9.DayNine.ChecksumCalculator;
import day_9.DayNine.Fragmentizer;
import day_9.DayNine.InputParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayNineTest {

    public static final String INPUT = "2333133121414131402";

    @Test
    void should_fragment_sample_by_free_space() {
        assertThat(Fragmentizer.fragmentByFreeSpace(InputParser.parse("12345"))).containsExactly(
                0, 2, 2, 1, 1, 1, 2, 2, 2
        );
    }

    @Test
    void should_fragment_input_by_free_space() {
        assertThat(Fragmentizer.fragmentByFreeSpace(InputParser.parse(INPUT))).containsExactly(
                0, 0, 9, 9, 8, 1, 1, 1, 8, 8, 8, 2, 7, 7, 7, 3, 3, 3, 6, 4, 4, 6, 5, 5, 5, 5, 6, 6
        );
    }

    @Test
    void should_fragment_input_by_whole_file() {
        assertThat(Fragmentizer.fragmentByWholeFile(InputParser.parse(INPUT))).containsExactly(
                0, 0, 9, 9, 2, 1, 1, 1, 7, 7, 7, 0, 4, 4, 0, 3, 3, 3, 0, 0, 0, 0, 5, 5, 5, 5, 0, 6, 6, 6, 6, 0, 0, 0, 0, 0, 8, 8, 8, 8, 0, 0
        );
    }

    @Test
    void should_calculate_free_space_fragmentation_checksum() {
        List<Integer> fragmentByFreeSpace = Fragmentizer.fragmentByFreeSpace(InputParser.parse(INPUT));
        assertThat(ChecksumCalculator.calculateChecksum(fragmentByFreeSpace)).isEqualTo(1928);
    }

    @Test
    void should_calculate_whole_file_fragmentation_checksum() {
        List<Integer> fragmentByWholeFiles = Fragmentizer.fragmentByWholeFile(InputParser.parse(INPUT));
        assertThat(ChecksumCalculator.calculateChecksum(fragmentByWholeFiles)).isEqualTo(2858);
    }

    // Wrong
    // 968821350541
}