package day_12;

import lombok.*;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static day_12.DayTwelve.Garden.BY_PERIMETER;
import static day_12.DayTwelve.Garden.BY_REGION_SIDES;

public class DayTwelve {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        Garden garden = InputParser.parse(FileUtil.readToList("day_12.txt"));
        System.out.println("Solution for part one is: " + garden.calculateTotalPrice(BY_PERIMETER));
        System.out.println("Solution for part two is: " + garden.calculateTotalPrice(BY_REGION_SIDES));
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Getter
    static class Garden {

        public static final Function<List<Position>, Integer> BY_PERIMETER = group ->
                group.size() * group.stream()
                        .map(Position::getPerimeter)
                        .reduce(Integer::sum)
                        .orElse(0);

        public static final Function<List<Position>, Integer> BY_REGION_SIDES = group ->
                group.size() * countRegionSides(group);

        private final List<List<Position>> map;
        private final List<List<Position>> groups = new ArrayList<>();

        public Garden(List<List<Position>> map) {
            this.map = map;
            groupPositions();
        }

        public int calculateTotalPrice(Function<List<Position>, Integer> calculationMethod) {
            return groups.stream()
                    .map(calculationMethod)
                    .reduce(Integer::sum)
                    .orElse(0);
        }

        private void groupPositions() {
            map.stream().flatMap(Collection::stream).forEach(position -> {
                if (position.hasNoGroup()) {
                    List<Position> group = new ArrayList<>();
                    position.addToGroup(group);
                    groupNeighbours(position, group);
                    groups.add(group);
                }
            });
        }

        private void groupNeighbours(Position position, List<Position> group) {
            int neighboursCount = 0;
            for (int i = -1; i <= 1; i += 2) {
                int horizontal = assignNeighbour(position.getX() + i, position.getY(), position.getPlant(), group);
                int vertical = assignNeighbour(position.getX(), position.getY() + i, position.getPlant(), group);
                if (vertical == 0) {
                    position.addBorder(i < 0 ? Direction.UP : Direction.DOWN);
                }
                neighboursCount += horizontal + vertical;
            }
            position.setPerimeter(4 - neighboursCount);
        }

        private int assignNeighbour(int x, int y, char plant, List<Position> group) {
            if (isOnMap(x, y) && map.get(y).get(x).getPlant() == plant) {
                Position position = map.get(y).get(x);
                if (position.hasNoGroup()) {
                    position.addToGroup(group);
                    groupNeighbours(position, group);
                }
                return 1;
            }
            return 0;
        }

        private boolean isOnMap(int x, int y) {
            return x >= 0 && x < map.get(0).size() && y >= 0 && y < map.size();
        }

        private static int countRegionSides(List<Position> group) {
            int sides = countBorderSidesHavingDirection(group, Direction.UP);
            sides += countBorderSidesHavingDirection(group, Direction.DOWN);
            return sides * 2;
        }

        private static int countBorderSidesHavingDirection(List<Position> group, Direction direction) {
            Map<Integer, List<Integer>> positions = group.stream()
                    .filter(position -> position.getBorders().contains(direction))
                    .collect(Collectors.groupingBy(
                            Position::getY,
                            Collectors.mapping(Position::getX, toSortedList())
                    ));
            int sides = positions.size();
            for (List<Integer> line : positions.values()) {
                for (int i = 1; i < line.size(); i++) {
                    if (line.get(i) - line.get(i - 1) > 1) {
                        sides++;
                    }
                }
            }
            return sides;
        }

        private static Collector<Integer, Object, List<Integer>> toSortedList() {
            return Collectors.collectingAndThen(Collectors.toList(), list -> {
                list.sort(Integer::compareTo);
                return list;
            });
        }

        public static Garden of(List<List<Position>> map) {
            return new Garden(map);
        }
    }

    @RequiredArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    static class Position {

        private final int x;
        private final int y;
        private final char plant;

        @EqualsAndHashCode.Exclude
        private int perimeter;
        @EqualsAndHashCode.Exclude
        @Setter(AccessLevel.NONE)
        private List<Direction> borders = new ArrayList<>();
        @EqualsAndHashCode.Exclude
        @Setter(AccessLevel.NONE)
        private List<Position> group;

        public void addToGroup(List<Position> group) {
            group.add(this);
            this.group = group;
        }

        public void addBorder(Direction direction) {
            borders.add(direction);
        }

        public boolean hasNoGroup() {
            return group == null;
        }

        public static Position of(int x, int y, char plant) {
            return new Position(x, y, plant);
        }
    }

    enum Direction {
        UP, DOWN
    }

    @UtilityClass
    static class InputParser {

        public static Garden parse(List<String> input) {
            List<List<Position>> map = new ArrayList<>();
            for (int y = 0; y < input.size(); y++) {
                List<Position> line = new ArrayList<>();
                for (int x = 0; x < input.get(0).length(); x++) {
                    line.add(Position.of(x, y, input.get(y).charAt(x)));
                }
                map.add(line);
            }
            return Garden.of(map);
        }
    }
}
