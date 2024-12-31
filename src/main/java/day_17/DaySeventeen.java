package day_17;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.ParserUtil.convertToIntegerList;
import static utils.ParserUtil.splitBySeparator;

public class DaySeventeen {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        Computer computer = InputParser.parse(FileUtil.readToList("day_17.txt"));
        System.out.println("Solution for part one is: " + computer.run());
        System.out.println("Solution for part two is: " + computer.findCopy());
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    @AllArgsConstructor
    @Getter
    static class Computer {

        private final List<Integer> output = new ArrayList<>();
        private final int initValueOfRegisterA;

        private long registerA;
        private long registerB;
        private long registerC;
        private List<Integer> program;

        public String run() {
            for (int i = 0; i < program.size(); i += 2) {
                int opcode = program.get(i);
                int operand = program.get(i + 1);
                i = process(opcode, operand, i);
            }
            return output.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }

        public long findCopy() {
            return searchCopy(0, program.size() - 1);
        }

        private long searchCopy(long curr, int expectedProgramIndex) {
            if (expectedProgramIndex < 0) {
                return curr;
            }
            for (int i = 0; i < 8; i++) {
                long next = curr << 3 | i;
                if (calculateFirstOutput(next) == program.get(expectedProgramIndex)) {
                    long result = searchCopy(next, expectedProgramIndex - 1);
                    if (result > 0) {
                        return result;
                    }
                }
            }
            return -1;
        }

        private int calculateFirstOutput(long regAValue) {
            reset(regAValue);
            for (int i = 0; i < program.size(); i += 2) {
                int opcode = program.get(i);
                int operand = program.get(i + 1);
                i = process(opcode, operand, i);
            }
            return output.get(0);
        }

        private int process(int opcode, int operand, int pointer) {
            if (opcode == 0) {
                // adv
                registerA = processRegisterADivision(operand);
                return pointer;
            }
            if (opcode == 1) {
                // bxl
                registerB = registerB ^ operand;
                return pointer;
            }
            if (opcode == 2) {
                // bst
                registerB = translateCombo(operand) % 8;
                return pointer;
            }
            if (opcode == 3) {
                // jnz
                return registerA == 0 ? pointer : operand - 2;
            }
            if (opcode == 4) {
                // bxc
                registerB = registerB ^ registerC;
                return pointer;
            }
            if (opcode == 5) {
                // out
                output.add((int) (translateCombo(operand) % 8));
                return pointer;
            }
            if (opcode == 6) {
                // bdv
                registerB = processRegisterADivision(operand);
                return pointer;
            }
            // cdv (opcode = 7)
            registerC = processRegisterADivision(operand);
            return pointer;
        }

        private long processRegisterADivision(int operand) {
            return operand == 4 ? 0 : (long) (registerA / Math.pow(2, translateCombo(operand)));
        }

        private long translateCombo(int operand) {
            return operand < 4 ? operand : operand == 4 ? registerA : operand == 5 ? registerB : registerC;
        }

        private void reset(long registerA) {
            this.registerA = registerA;
            registerB = 0;
            registerC = 0;
            output.clear();
        }

        void analyze(long i) {
            reset(i);
            System.out.println(
                    i + ", octal=" + Long.toOctalString(i) +
                    ", binary=" + Long.toBinaryString(i) +
                    ", output: [ " + run() + " ]"
            );
        }
    }

    @UtilityClass
    static class InputParser {

        private static final String DELIMITER = ": ";

        public static Computer parse(List<String> input) {
            int registerA = Integer.parseInt(input.get(0).split(DELIMITER)[1]);
            int registerB = Integer.parseInt(input.get(1).split(DELIMITER)[1]);
            int registerC = Integer.parseInt(input.get(2).split(DELIMITER)[1]);
            List<Integer> program = convertToIntegerList(splitBySeparator(input.get(4).split(DELIMITER)[1], ","));
            return new Computer(registerA, registerA, registerB, registerC, program);
        }
    }
}
