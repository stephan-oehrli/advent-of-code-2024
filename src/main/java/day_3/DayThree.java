package day_3;

import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class DayThree {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> input = FileUtil.readToList("day_3.txt");
        List<MulOperation> mulOperations = InputParser.parseMulOperations(input);
        Integer result1 = mulOperations.stream().map(MulOperation::calculateProduct).reduce(Integer::sum).orElseThrow();
        mulOperations = InputParser.parseConditionalMulOperations(input);
        Integer result2 = mulOperations.stream().map(MulOperation::calculateProduct).reduce(Integer::sum).orElseThrow();

        System.out.println("Solution for part one is: " + result1);
        System.out.println("Solution for part two is: " + result2);
    }

    record MulOperation(int factorOne, int factorTwo) {

        public int calculateProduct() {
            return factorOne * factorTwo;
        }
        
        public static MulOperation of(MatchResult result) {
            return new MulOperation(parseInt(result.group(1)), parseInt(result.group(2)));
        }
    }

    @UtilityClass
    static class InputParser {

        private static final Pattern MUL_OPERATION_PATTERN = 
                Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        private static final Pattern CONDITIONAL_MUL_OPERATION_PATTERN = 
                Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|(don't\\(\\))|(do\\(\\))");

        public static List<MulOperation> parseMulOperations(List<String> input) {
            return input.stream()
                    .flatMap(line -> MUL_OPERATION_PATTERN.matcher(line).results())
                    .map(MulOperation::of)
                    .toList();
        }

        public static List<MulOperation> parseConditionalMulOperations(List<String> input) {
            boolean isEnabled = true;
            List<MulOperation> mulOperations = new ArrayList<>();
            for (String line : input) {
                Matcher matcher = CONDITIONAL_MUL_OPERATION_PATTERN.matcher(line);
                List<MatchResult> matchResults = matcher.results().toList();
                for (MatchResult result : matchResults) {
                    if (result.group().contains("don't()")) {
                        isEnabled = false;
                    } else if (result.group().contains("do()")) {
                        isEnabled = true;
                    } else if (isEnabled) {
                        mulOperations.add(MulOperation.of(result));
                    }
                }
            }
            return mulOperations;
        }
    }
}
