package day_7;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;
import utils.ParserUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static day_7.DaySeven.Operator.ADD;
import static day_7.DaySeven.Operator.MULTIPLY;

public class DaySeven {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        List<Operation> operations = InputParser.parse(FileUtil.readToList("day_7.txt"));
        Long resultOne = operations.stream()
                .filter(operation -> operation.isValidEquation(2))
                .map(Operation::result)
                .reduce(Math::addExact).orElseThrow();
        Long resultTwo = operations.stream()
                .filter(operation -> operation.isValidEquation(3))
                .map(Operation::result)
                .reduce(Math::addExact).orElseThrow();

        System.out.println("Solution for part one is: " + resultOne);
        System.out.println("Solution for part two is: " + resultTwo);
        System.out.println(System.currentTimeMillis() - start + "ms");
    }

    record Operation(Long result, List<Integer> operands) {

        public boolean isValidEquation(int numOfOperatorsToCheck) {
            int possibleOperations = (int) Math.pow(numOfOperatorsToCheck, operands.size() - 1);
            int bitmaskLength = Integer.toString(possibleOperations, numOfOperatorsToCheck).length() - 1;
            for (int i = 0; i < possibleOperations; i++) {
                String bitmask = StringUtils.leftPad(Integer.toString(i, numOfOperatorsToCheck), bitmaskLength, '0');
                if (isValidEquation(bitmask)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isValidEquation(String bitmask) {
            List<Operator> operators = bitmask.chars()
                    .mapToObj(c -> Operator.values()[Character.getNumericValue(c)])
                    .toList();

            long equationResult = operands.get(0);
            for (int i = 1; i < operands.size(); i++) {
                equationResult = apply(operators.get(i - 1), equationResult, operands.get(i));
                if (equationResult > result) {
                    return false;
                }
            }
            return result.equals(equationResult);
        }

        private long apply(Operator operator, long operand1, long operand2) {
            return operator == ADD ? Math.addExact(operand1, operand2) :
                    operator == MULTIPLY ? Math.multiplyExact(operand1, operand2) :
                            Long.parseLong(operand1 + "" + operand2);
        }

        public static Operation of(Long result, List<Integer> operands) {
            return new Operation(result, operands);
        }
    }

    @RequiredArgsConstructor
    @Getter
    enum Operator {
        ADD("+"), MULTIPLY("*"), CONCAT("||");

        private final String sign;
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
