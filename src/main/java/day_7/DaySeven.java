package day_7;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;
import utils.ParserUtil;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Stack;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class DaySeven {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        List<Operation> operations = InputParser.parse(FileUtil.readToList("day_7.txt"));
        Long resultOne = operations.stream()
                .filter(Operation::isValidEquation)
                .map(Operation::getResult)
                .reduce(Math::addExact).orElseThrow();
        Long resultTwo = operations.stream()
                .filter(Operation::isValidEquationWithConcatenation)
                .map(Operation::getResult)
                .reduce(Math::addExact).orElseThrow();

        System.out.println("Solution for part one is: " + resultOne);
        System.out.println("Solution for part two is: " + resultTwo);
        System.out.println(System.currentTimeMillis() - start + "ms");
    }

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    static final class Operation {

        private final Long result;
        private final List<Integer> operands;

        private final Stack<Integer> stack = new Stack<>();
        private boolean includeConcatenation;

        public boolean isValidEquation() {
            return isValidEquation(false);
        }

        public boolean isValidEquationWithConcatenation() {
            return isValidEquation(true);
        }

        private boolean isValidEquation(boolean includeConcatenation) {
            stack.clear();
            stack.addAll(operands);
            this.includeConcatenation = includeConcatenation;
            return checkValidEquation(result);
        }

        private boolean checkValidEquation(Long number) {
            Integer operand = stack.pop();
            if (stack.isEmpty()) {
                if (operand.equals(number.intValue())) {
                    return true;
                }
                stack.push(operand);
                return false;
            }
            if (includeConcatenation && String.valueOf(number).endsWith(String.valueOf(operand))) {
                String numberString = String.valueOf(number);
                int newLength = numberString.length() - String.valueOf(operand).length();
                Long newNumber = Long.valueOf(defaultIfEmpty(numberString.substring(0, newLength), "0"));
                if (checkValidEquation(newNumber)) {
                    return true;
                }
            }
            if (number % operand == 0 && checkValidEquation(number / operand)) {
                return true;
            }
            if (number > operand && checkValidEquation(number - operand)) {
                return true;
            }
            stack.push(operand);
            return false;
        }

        public static Operation of(Long result, List<Integer> operands) {
            return new Operation(result, operands);
        }
    }

    @UtilityClass
    static class InputParser {

        public static List<Operation> parse(List<String> input) {
            return input.stream()
                    .map(ParserUtil::splitByWhiteSpace)
                    .map(InputParser::convertToOperation)
                    .toList();
        }

        private static Operation convertToOperation(List<String> line) {
            long result = Long.parseLong(StringUtils.chop(line.remove(0)));
            List<Integer> operands = line.stream()
                    .map(Integer::parseInt)
                    .toList();
            return Operation.of(result, operands);
        }
    }
}
