package day_17;

import day_17.DaySeventeen.Computer;
import day_17.DaySeventeen.InputParser;
import org.junit.jupiter.api.Test;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DaySeventeenTest {

    public static final List<String> INPUT = Arrays.asList(
            "Register A: 729",
            "Register B: 0",
            "Register C: 0",
            "",
            "Program: 0,1,5,4,3,0"
    );

    public static final List<String> INPUT_2 = Arrays.asList(
            "Register A: 2024",
            "Register B: 0",
            "Register C: 0",
            "",
            "Program: 0,3,5,4,3,0"
    );

    @Test
    void should_parse_computer() {
        Computer computer = InputParser.parse(INPUT);
        assertThat(computer)
                .extracting(Computer::getRegisterA, Computer::getRegisterB, Computer::getRegisterC)
                .containsExactly(729L, 0L, 0L);
        assertThat(computer.getProgram()).containsExactly(0, 1, 5, 4, 3, 0);
    }

    @Test
    void should_calculate_output() {
        Computer computer = InputParser.parse(INPUT);
        assertThat(computer.run()).isEqualTo("4,6,3,5,6,3,5,2,1,0");
    }

    @Test
    void should_find_copy() {
        Computer computer = InputParser.parse(INPUT_2);
        assertThat(computer.findCopy()).isEqualTo(117440);
    }

    @Test
    void analyze() throws FileNotFoundException {
        /*
        
            do {
                B = A % 8;
                B = B ^ 3;
                C = (int) A / Math.pow(2, B);
                B = B ^ 5;
                A = (int) A / 8;
                B = B ^ C;
                out(B % 8);
            } while (A != 0);
            
           Example: 
           A = 101 111 100 010 (3042)

             0:     B = 010              (last chunk of A)
             2:     B = 001              (B [010] XOR 3 [011])
             4:     C = 010 111 110 001  (right shift A by value of B)
             6:     B = 100              (B [001] XOR 5 [101])
             8:     A = 101 111 100      (cut last chunk of A)
            10:     B = 010 111 110 101  (B [100] XOR C [001])
            12:     OUT(101)             (last chunk of B)
            
         */

        Computer computer = InputParser.parse(FileUtil.readToList("day_17.txt"));
        for (int i = 0; i < 16; i++) {
            computer.analyze(0b110L << (i * 3));
        }
    }
}