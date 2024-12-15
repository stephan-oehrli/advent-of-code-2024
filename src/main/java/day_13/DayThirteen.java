package day_13;

import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DayThirteen {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        List<String> input = FileUtil.readToList("day_13.txt");
        List<ClawMachine> normalPrizeMachines = InputParser.parseWithNormalPrize(input);
        List<ClawMachine> highPrizeMachines = InputParser.parseWithIncreasedPrize(input);
        System.out.println("Solution for part one is: " + Calculator.calculateFewestTokensToWin(normalPrizeMachines));
        System.out.println("Solution for part two is: " + Calculator.calculateFewestTokensToWin(highPrizeMachines));
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    static class Calculator {

        public static long calculateFewestTokensToWin(List<ClawMachine> clawMachines) {
            return clawMachines.stream()
                    .map(ClawMachine::calculateFewestTokensToWin)
                    .reduce(Long::sum).orElseThrow();
        }
    }

    record ClawMachine(Point2D buttonA, Point2D buttonB, Point2D prize) {

        public long calculateFewestTokensToWin() {
            double bDividend = buttonA.getX() * prize.getY() - buttonA.getY() * prize.getX();
            double bDivisor = buttonA.getY() * -buttonB.getX() + buttonA.getX() * buttonB.getY();
            if (bDividend % bDivisor != 0) {
                return 0;
            }
            long bPresses = (long) (bDividend / bDivisor);
            double aDividend = prize.getY() - bPresses * buttonB.getY();
            if (aDividend % buttonA.getY() != 0) {
                return 0;
            }
            long aPresses = (long) (aDividend / buttonA.getY());
            return 3 * aPresses + bPresses;
        }
    }

    @UtilityClass
    static class InputParser {

        public static final Pattern PLUS_PREFIX_PATTERN = Pattern.compile("\\+(\\d*)");
        public static final Pattern EQUAL_SIGN_PREFIX_PATTERN = Pattern.compile("=(\\d*)");

        private static final double INCREMENT = 10000000000000.0;

        public static List<ClawMachine> parseWithNormalPrize(List<String> input) {
            return parse(input, false);
        }

        public static List<ClawMachine> parseWithIncreasedPrize(List<String> input) {
            return parse(input, true);
        }

        private static List<ClawMachine> parse(List<String> input, boolean hasIncreasedPrize) {
            List<ClawMachine> clawMachines = new ArrayList<>();
            Point2D buttonA = null;
            Point2D buttonB = null;
            Point2D prize = null;
            for (String line : input) {
                if (line.isEmpty()) {
                    clawMachines.add(new ClawMachine(buttonA, buttonB, prize));
                } else if (line.startsWith("Button A:")) {
                    buttonA = parsePoint(PLUS_PREFIX_PATTERN, line);
                } else if (line.startsWith("Button B:")) {
                    buttonB = parsePoint(PLUS_PREFIX_PATTERN, line);
                } else {
                    prize = parsePoint(EQUAL_SIGN_PREFIX_PATTERN, line);
                    if (hasIncreasedPrize) {
                        prize.setLocation(INCREMENT + prize.getX(), INCREMENT + prize.getY());
                    }
                }
            }
            clawMachines.add(new ClawMachine(buttonA, buttonB, prize));
            return clawMachines;
        }

        private static Point2D parsePoint(Pattern prefixPattern, String line) {
            return prefixPattern.matcher(line).results()
                    .map(r -> Double.parseDouble(r.group(1)))
                    .collect(toPoint());
        }

        private static Collector<Double, Object, Point2D> toPoint() {
            return Collectors.collectingAndThen(Collectors.toList(),
                    list -> new Point2D.Double(list.get(0), list.get(1))
            );
        }
    }
}
