package day_15;

import lombok.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DayFifteen {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        List<String> input = FileUtil.readToList("day_15.txt");
        Warehouse smallWarehouse = InputParser.parseSmallWarehouse(input);
        smallWarehouse.moveBoxes();
        System.out.println("Solution for part one is: " + smallWarehouse.calculateSumOfBoxCoordinates());
        Warehouse bigWarehouse = InputParser.parseBigWarehouse(input);
        bigWarehouse.moveBoxes();
        System.out.println("Solution for part two is: " + bigWarehouse.calculateSumOfBoxCoordinates());
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    @AllArgsConstructor
    @Getter
    static class Warehouse {

        private final List<List<Position>> positions;
        private final String movements;

        private Position robotPosition;

        public int calculateSumOfBoxCoordinates() {
            return positions.stream().flatMap(Collection::stream)
                    .filter(position -> position.hasSymbol('O') || position.hasSymbol('['))
                    .map(position -> 100 * position.getY() + position.getX())
                    .reduce(Integer::sum)
                    .orElseThrow();
        }

        public void moveBoxes() {
            for (char instruction : movements.toCharArray()) {
                move(robotPosition, Direction.of(instruction));
            }
        }

        private void move(Position position, Direction direction) {
            Position nextPosition = findNextPosition(position, direction);
            if (nextPosition != null && !nextPosition.hasSymbol('#')) {
                if (nextPosition.hasSymbol('O') || (!direction.isVertical() && nextPosition.containsBoxPart())) {
                    move(nextPosition, direction);
                }
                if (direction.isVertical() && nextPosition.containsBoxPart()) {
                    Position relatedBoxPart = findRelatedBoxPart(nextPosition);
                    if (isEachMovable(nextPosition, relatedBoxPart, direction)) {
                        move(nextPosition, direction);
                        move(relatedBoxPart, direction);
                    }
                }
                if (nextPosition.hasSymbol('.')) {
                    nextPosition.setSymbol(position.getSymbol());
                    if (nextPosition.hasSymbol('@')) {
                        robotPosition = nextPosition;
                    }
                    position.setSymbol('.');
                }
            }
        }

        private boolean isEachMovable(Position boxPartOne, Position boxPartTwo, Direction direction) {
            List<Position> nextPositions = List.of(
                    findNextPosition(boxPartOne, direction),
                    findNextPosition(boxPartTwo, direction)
            );

            if (nextPositions.stream().anyMatch(position -> position.hasSymbol('#'))) {
                // next positions contain obstacles -> not moveable
                return false;
            }
            if (nextPositions.stream().noneMatch(Position::containsBoxPart)) {
                // next positions are free -> moveable
                return true;
            }

            // next positions contain other boxes -> check if those are moveable too
            return nextPositions.stream()
                    .filter(Position::containsBoxPart)
                    .allMatch(position -> isEachMovable(position, findRelatedBoxPart(position), direction));
        }

        private Position findNextPosition(Position position, Direction direction) {
            if (direction.isVertical()) {
                return findPosition(position.getX(), position.getY() + (direction == Direction.UP ? -1 : 1));
            }
            return findPosition(position.getX() + (direction == Direction.LEFT ? -1 : 1), position.getY());
        }

        private Position findRelatedBoxPart(Position position) {
            return positions.get(position.getY()).get(position.getX() + (position.hasSymbol('[') ? 1 : -1));
        }

        private Position findPosition(int x, int y) {
            return isOnMap(x, y) ? positions.get(y).get(x) : null;
        }

        private boolean isOnMap(int x, int y) {
            return y >= 0 && y < positions.size() && x >= 0 && x < positions.get(y).size();
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    static class Position {

        private final int x;
        private final int y;
        @EqualsAndHashCode.Exclude
        private char symbol;

        public boolean hasSymbol(char symbol) {
            return this.symbol == symbol;
        }

        public boolean containsBoxPart() {
            return hasSymbol('[') || hasSymbol(']');
        }
    }

    @RequiredArgsConstructor
    @Getter
    enum Direction {

        UP('^'), RIGHT('>'), DOWN('v'), LEFT('<');

        private final char symbol;

        public static Direction of(char symbol) {
            for (Direction direction : Direction.values()) {
                if (direction.getSymbol() == symbol) {
                    return direction;
                }
            }
            throw new IllegalStateException("No direction found for symbol: " + symbol);
        }

        public boolean isVertical() {
            return this == UP || this == DOWN;
        }
    }

    @UtilityClass
    static class InputParser {

        public static Warehouse parseSmallWarehouse(List<String> input) {
            List<List<Position>> positions = new ArrayList<>();
            int inputSeparator = input.indexOf(StringUtils.EMPTY);

            Position robotPosition = null;
            for (int y = 0; y < inputSeparator; y++) {
                List<Position> line = new ArrayList<>();
                for (int x = 0; x < input.get(0).length(); x++) {
                    Position position = new Position(x, y, input.get(y).charAt(x));
                    line.add(position);
                    if (position.hasSymbol('@')) {
                        robotPosition = position;
                    }
                }
                positions.add(line);
            }

            return new Warehouse(positions, parseMovements(input, inputSeparator), robotPosition);
        }

        public static Warehouse parseBigWarehouse(List<String> input) {
            List<List<Position>> positions = new ArrayList<>();
            int inputSeparator = input.indexOf(StringUtils.EMPTY);

            Position robotPosition = null;
            for (int y = 0; y < inputSeparator; y++) {
                List<Position> line = new ArrayList<>();
                int xOffset = 0;
                for (int x = 0; x < input.get(0).length(); x++) {
                    Position position = new Position(x + xOffset++, y, input.get(y).charAt(x));
                    line.add(position);
                    if (position.hasSymbol('@')) {
                        robotPosition = position;
                        line.add(new Position(x + xOffset, y, '.'));
                    } else if (position.hasSymbol('#')) {
                        line.add(new Position(x + xOffset, y, '#'));
                    } else if (position.hasSymbol('O')) {
                        position.setSymbol('[');
                        line.add(new Position(x + xOffset, y, ']'));
                    } else if (position.hasSymbol('.')) {
                        line.add(new Position(x + xOffset, y, '.'));
                    }
                }
                positions.add(line);
            }

            return new Warehouse(positions, parseMovements(input, inputSeparator), robotPosition);
        }

        private static String parseMovements(List<String> input, int inputSeparator) {
            return IntStream.range(inputSeparator + 1, input.size())
                    .mapToObj(input::get)
                    .collect(Collectors.joining());
        }
    }
}
