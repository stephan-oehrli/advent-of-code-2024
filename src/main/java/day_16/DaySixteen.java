package day_16;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.*;

import static day_16.DaySixteen.Direction.*;

public class DaySixteen {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        PathFinder pathFinder = InputParser.parse(FileUtil.readToList("day_16.txt"));
        System.out.println("Solution for part one is: " + pathFinder.findBestPathScore());
        System.out.println("Solution for part two is: " + pathFinder.findNumberOfBestPathTiles());
        System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
    }

    @RequiredArgsConstructor
    @Getter
    static class PathFinder {

        private final List<List<Position>> mazeMap;
        private final Position start;
        private final Position end;
        private final Point endPoint;

        private final PriorityQueue<Position> priorityQueue = new PriorityQueue<>();
        private final Set<Position> bestPathTiles = new HashSet<>();
        private boolean isEndFound;

        public int findBestPathScore() {
            isEndFound = false;
            start.setValue(0.0);
            start.setDirection(RIGHT);
            priorityQueue.add(start);
            while (!isEndFound && !priorityQueue.isEmpty()) {
                checkNeighbours(priorityQueue.poll());
            }
            return end.getScore();
        }

        public int findNumberOfBestPathTiles() {
            if (!isEndFound) {
                findBestPathScore();
            }
            findBestPathTiles(end, null);
            bestPathTiles.add(start);
            return bestPathTiles.size();
        }

        private void findBestPathTiles(Position position, Integer previousScore) {
            while (!position.equals(start)) {
                bestPathTiles.add(position);
                findAlternativePath(position.left(), previousScore);
                findAlternativePath(position.right(), previousScore);
                findAlternativePath(position.ahead(), previousScore);
                previousScore = position.getScore();
                position = position.getPreviousPosition();
            }
        }

        private void findAlternativePath(Point point, Integer previousScore) {
            if (isNotOnMap(point) || previousScore == null) {
                return;
            }
            Position neighbour = mazeMap.get(point.y).get(point.x);
            int scoreDelta = previousScore - neighbour.getScore();
            if (neighbour.isVisited() && scoreDelta == 2 && !bestPathTiles.contains(neighbour)) {
                findBestPathTiles(neighbour, previousScore);
            }
        }

        private void checkNeighbours(Position position) {
            position.setVisited(true);
            analyzeBestPath(position, position.ahead(), 1, position.getDirection());
            analyzeBestPath(position, position.left(), 1001, position.turnLeft());
            analyzeBestPath(position, position.right(), 1001, position.turnRight());
        }

        private void analyzeBestPath(Position position, Point point, int increment, Direction direction) {
            if (isNotOnMap(point)) {
                return;
            }
            Position neighbour = mazeMap.get(point.y).get(point.x);
            if (neighbour.hasSymbol('#') || neighbour.isVisited()) {
                return;
            }
            int score = position.getScore() + increment;
            double value = endPoint.distance(point) + score;
            if (neighbour.getValue() == null || value < neighbour.getValue()) {
                neighbour.setScore(score);
                neighbour.setValue(value);
                neighbour.setDirection(direction);
                neighbour.setPreviousPosition(position);
                if (!priorityQueue.contains(neighbour)) {
                    priorityQueue.add(neighbour);
                }
                if (neighbour.equals(end)) {
                    isEndFound = true;
                }
            }
        }

        private boolean isNotOnMap(Point point) {
            return point.y < 0 || point.y >= mazeMap.size() || point.x < 0 || point.x >= mazeMap.get(point.y).size();
        }
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    @Setter
    @EqualsAndHashCode(of = {"x", "y"})
    static class Position implements Comparable<Position> {

        private final int x;
        private final int y;

        private final char symbol;

        private int score = 0;
        private Double value;
        private boolean isVisited;
        private Direction direction;
        private Position previousPosition;

        public boolean hasSymbol(char symbol) {
            return this.symbol == symbol;
        }

        public Point ahead() {
            int nextX = x + (direction.isVertical() ? 0 : direction == LEFT ? -1 : 1);
            int nextY = y + (!direction.isVertical() ? 0 : direction == UP ? -1 : 1);
            return new Point(nextX, nextY);
        }

        public Point left() {
            int nextX = x + (!direction.isVertical() ? 0 : direction == UP ? -1 : 1);
            int nextY = y + (direction.isVertical() ? 0 : direction == LEFT ? 1 : -1);
            return new Point(nextX, nextY);
        }

        public Point right() {
            int nextX = x + (!direction.isVertical() ? 0 : direction == UP ? 1 : -1);
            int nextY = y + (direction.isVertical() ? 0 : direction == LEFT ? -1 : 1);
            return new Point(nextX, nextY);
        }

        public Direction turnRight() {
            Direction[] directions = Direction.values();
            return directions[(direction.ordinal() + 1) % directions.length];
        }

        public Direction turnLeft() {
            Direction[] directions = Direction.values();
            return directions[(direction.ordinal() + directions.length - 1) % directions.length];
        }

        @Override
        public int compareTo(Position o) {
            return Double.compare(value, o.value);
        }
    }

    enum Direction {
        UP, RIGHT, DOWN, LEFT;

        public boolean isVertical() {
            return this == UP || this == DOWN;
        }
    }

    @UtilityClass
    static class InputParser {

        public static PathFinder parse(List<String> input) {
            List<List<Position>> mazeMap = new ArrayList<>();
            Position start = null;
            Position end = null;
            for (int y = 1; y < input.size() - 1; y++) {
                List<Position> line = new ArrayList<>();
                for (int x = 1; x < input.get(y).length() - 1; x++) {
                    char symbol = input.get(y).charAt(x);
                    Position position = Position.of(x - 1, y - 1, symbol);
                    line.add(position);
                    if (symbol == 'S') {
                        start = position;
                    } else if (symbol == 'E') {
                        end = position;
                    }
                }
                mazeMap.add(line);
            }
            assert end != null;
            return new PathFinder(mazeMap, start, end, new Point(end.x, end.y));
        }
    }
}
