package day_14;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DayFourteen {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        List<String> input = FileUtil.readToList("day_14.txt");
        System.out.println("Solution for part one is: " + Bathroom.of(input, 101, 103).calculateSafetyFactor(100));
        System.out.println("Solution for part two is: " + Bathroom.of(input, 101, 103).findChristmasTree());
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    record Robot(Point position, Point velocity, Bathroom bathroom) {

        public void move(int seconds) {
            double increasedPositionX = seconds * bathroom.getWidth() + position.getX();
            double increasedPositionY = seconds * bathroom.getHeight() + position.getY();
            double x = (increasedPositionX + seconds * velocity.getX()) % bathroom.getWidth();
            double y = (increasedPositionY + seconds * velocity.getY()) % bathroom.getHeight();
            position.setLocation((int) x, (int) y);
        }

        private int findQuadrant() {
            if (positionX() < bathroom.getYAxis() && positionY() < bathroom.getXAxis()) {
                return 1;
            }
            if (positionX() > bathroom.getYAxis() && positionY() < bathroom.getXAxis()) {
                return 2;
            }
            if (positionX() < bathroom.getYAxis() && positionY() > bathroom.getXAxis()) {
                return 3;
            }
            return 4;
        }

        public boolean isOnAxis() {
            return positionX() == bathroom.getYAxis() || positionY() == bathroom.getXAxis();
        }

        public int positionX() {
            return (int) position.getX();
        }

        public int positionY() {
            return (int) position.getY();
        }
    }

    @Getter
    static class Bathroom {

        private final List<Robot> robots;
        private final int totalAmountOFRobots;

        private final int width;
        private final int height;
        private final int xAxis;
        private final int yAxis;

        private Map<Integer, Set<Integer>> robotMap;

        Bathroom(List<String> input, int width, int height) {
            this.robots = InputParser.parse(input, this);
            this.width = width;
            this.height = height;
            this.totalAmountOFRobots = input.size();
            this.xAxis = height / 2;
            this.yAxis = width / 2;
        }

        public int calculateSafetyFactor(int seconds) {
            moveBy(seconds);
            Map<Integer, List<Robot>> quadrants = robots.stream()
                    .filter(robot -> !robot.isOnAxis())
                    .collect(Collectors.groupingBy(Robot::findQuadrant));
            return quadrants.values().stream()
                    .map(List::size)
                    .reduce(Math::multiplyExact)
                    .orElseThrow();
        }

        public int findChristmasTree() {
            int seconds = 0;
            boolean isFound = false;
            while (!isFound) {
                moveBy(1);
                seconds++;
                isFound = isPossiblyImage();
            }
            printMap();
            return seconds;
        }

        private void moveBy(int seconds) {
            robots.forEach(robot -> robot.move(seconds));
        }

        private boolean isPossiblyImage() {
            robotMap = robots.stream()
                    .collect(Collectors.groupingBy(
                            Robot::positionY,
                            Collectors.mapping(Robot::positionX, Collectors.toSet()))
                    );

            int size = robotMap.values().stream().flatMap(Collection::stream).toList().size();
            return size == totalAmountOFRobots;
        }

        private void printMap() {
            Set<Integer> emptySet = new HashSet<>();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    System.out.print(robotMap.getOrDefault(y, emptySet).contains(x) ? "x" : " ");
                }
                System.out.println();
            }
        }

        public static Bathroom of(List<String> input, int width, int height) {
            return new Bathroom(input, width, height);
        }
    }

    @UtilityClass
    static class InputParser {

        private static final Pattern LINE_PATTERN = Pattern.compile("p=(\\d*),(\\d*) v=(-?\\d*),(-?\\d*)");

        public static List<Robot> parse(List<String> input, Bathroom bathroom) {
            return input.stream()
                    .map(LINE_PATTERN::matcher)
                    .flatMap(Matcher::results)
                    .map((MatchResult result) -> toRobot(result, bathroom))
                    .toList();
        }

        private static Robot toRobot(MatchResult matchResult, Bathroom bathroom) {
            Point position = new Point(Integer.parseInt(matchResult.group(1)), Integer.parseInt(matchResult.group(2)));
            Point velocity = new Point(Integer.parseInt(matchResult.group(3)), Integer.parseInt(matchResult.group(4)));
            return new Robot(position, velocity, bathroom);
        }
    }
}
