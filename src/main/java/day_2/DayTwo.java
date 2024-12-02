package day_2;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import utils.FileUtil;
import utils.ParserUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

import static day_2.DayTwo.Order.*;

public class DayTwo {

    public static void main(String[] args) throws FileNotFoundException {
        List<Report> reports = InputParser.parse(FileUtil.readToList("day_2.txt"));
        List<Report> safeReports = reports.stream().filter(Report::isSafe).toList();
        System.out.println("Solution for part one is: " + safeReports.size());
        safeReports = reports.stream().filter(Report::isSafeWithTolerance).toList();
        System.out.println("Solution for part two is: " + safeReports.size());
    }

    static class Report {

        @Getter
        private final List<Integer> levels;
        private Order order;

        private final List<BiPredicate<Integer, Integer>> RULE_BREAKERS = Arrays.asList(
                (first, second) -> order == NONE || first.equals(second),
                (first, second) -> first < second && order == DECREASING,
                (first, second) -> first > second && order == INCREASING,
                (first, second) -> Math.abs(first - second) > 3
        );

        Report(List<Integer> levels) {
            this.levels = levels;
            this.order = findOrder(levels);
        }

        private Order findOrder(List<Integer> levels) {
            Integer first = levels.get(0);
            Integer second = levels.get(1);
            if (first.equals(second)) {
                return NONE;
            }
            return first < second ? INCREASING : DECREASING;
        }

        public boolean isSafe() {
            return checkSafety(false);
        }

        public boolean isSafeWithTolerance() {
            return checkSafety(true);
        }

        private boolean checkSafety(boolean withTolerance) {
            for (int i = 0; i < levels.size() - 1; i++) {
                Integer first = levels.get(i);
                Integer second = levels.get(i + 1);
                if (RULE_BREAKERS.stream().anyMatch(p -> p.test(first, second))) {
                    return withTolerance && checkTolerance();
                }
            }
            return true;
        }

        private boolean checkTolerance() {
            return IntStream.range(0, levels.size())
                    .mapToObj(this::createReportWithoutLevel)
                    .anyMatch(Report::isSafe);
        }

        private Report createReportWithoutLevel(int indexToRemove) {
            ArrayList<Integer> levelsCopy = new ArrayList<>(levels);
            levelsCopy.remove(indexToRemove);
            return new Report(levelsCopy);
        }
    }

    enum Order {
        INCREASING, DECREASING, NONE
    }

    @UtilityClass
    static class InputParser {

        public static List<Report> parse(List<String> input) {
            return input.stream()
                    .map(ParserUtil::splitByWhiteSpace)
                    .map(ParserUtil::convertToIntegerList)
                    .map(Report::new)
                    .toList();
        }
    }
}
