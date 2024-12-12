package day_10;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DayTen {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        TopographicMap topographicMap = InputParser.parse(FileUtil.readToList("day_10.txt"));
        System.out.println("Solution for part one is: " + topographicMap.findTrails());
        System.out.println("Solution for part two is: " + topographicMap.findRatings());
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    record TopographicMap(List<List<Position>> map, List<Position> trailHeads) {

        public int findTrails() {
            return goThroughMap(true);
        }

        public int findRatings() {
            return goThroughMap(false);
        }

        private int goThroughMap(boolean isTrailsCounting) {
            int result = 0;
            for (Position position : trailHeads) {
                if (isTrailsCounting) {
                    result += findTrailsStartingFrom(position);
                    resetVisited();
                } else {
                    result += findRatingsStartingFrom(position);
                }
            }
            return result;
        }

        private int findTrailsStartingFrom(Position position) {
            if (position.getValue() == 9) {
                int result = position.isVisited() ? 0 : 1;
                position.setVisited(true);
                return result;
            }
            return countWith(findNeighbours(position), this::findTrailsStartingFrom);
        }

        private int findRatingsStartingFrom(Position position) {
            if (position.getValue() == 8) {
                return findNeighbours(position).size();
            }
            return countWith(findNeighbours(position), this::findRatingsStartingFrom);
        }

        private int countWith(List<Position> neighbours, Function<Position, Integer> countFunction) {
            int counter = 0;
            for (Position neighbour : neighbours) {
                counter += countFunction.apply(neighbour);
            }
            return counter;
        }

        private List<Position> findNeighbours(Position position) {
            List<Position> neighbours = new ArrayList<>();
            for (int i = -1; i <= 1; i += 2) {
                findUnvisitedNeighbour(position.getX() + i, position.getY(), position).ifPresent(neighbours::add);
                findUnvisitedNeighbour(position.getX(), position.getY() + i, position).ifPresent(neighbours::add);
            }
            return neighbours;
        }

        private Optional<Position> findUnvisitedNeighbour(int x, int y, Position position) {
            if (isOnMap(x, y)) {
                Position potentialNeighbour = map.get(y).get(x);
                if (potentialNeighbour.getValue() == position.getValue() + 1 && !potentialNeighbour.isVisited()) {
                    return Optional.of(map.get(y).get(x));
                }
            }
            return Optional.empty();
        }

        private boolean isOnMap(int x, int y) {
            return x >= 0 && y >= 0 && x < map.get(0).size() && y < map.size();
        }

        private void resetVisited() {
            this.map.stream()
                    .flatMap(Collection::stream)
                    .filter(Position::isVisited)
                    .forEach(position -> position.setVisited(false));
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    static class Position {

        private final int x;
        private final int y;
        private final int value;
        private boolean isVisited;

        public static Position of(int x, int y, int value) {
            return new Position(x, y, value, false);
        }
    }

    @UtilityClass
    static class InputParser {

        public static TopographicMap parse(List<String> input) {
            List<List<Position>> map = new ArrayList<>();
            List<Position> trailHeads = new ArrayList<>();
            for (int y = 0; y < input.size(); y++) {
                ArrayList<Position> line = new ArrayList<>();
                for (int x = 0; x < input.get(0).length(); x++) {
                    char symbol = input.get(y).charAt(x);
                    int number = symbol == '.' ? -1 : Character.getNumericValue(symbol);
                    Position position = Position.of(x, y, number);
                    line.add(position);
                    if (number == 0) {
                        trailHeads.add(position);
                    }
                }
                map.add(line);
            }
            return new TopographicMap(map, trailHeads);
        }
    }
}
