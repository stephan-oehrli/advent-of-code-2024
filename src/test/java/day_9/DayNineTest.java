package day_9;

import day_9.DayNine.ChecksumCalculator;
import day_9.DayNine.InputParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DayNineTest {

    @Test
    void should_parse_int_array() {
        assertThat(InputParser.parse("12345"))
                .containsExactly(0, 2, 2, 1, 1, 1, 2, 2, 2);
    }

    @Test
    void should_calculate_checksum() {
        long checksum = ChecksumCalculator.calculateChecksum(InputParser.parse("2333133121414131402"));
        assertThat(checksum).isEqualTo(1928);
    }
}