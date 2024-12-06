package day_6;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.*;

import static day_6.DaySix.Direction.*;

public class DaySix {

    public static void main(String[] args) throws FileNotFoundException {
        ManufacturingLab manufacturingLab = InputParser.parse(FileUtil.readToList("day_6.txt"));
        System.out.println("Solution for part one is: " + manufacturingLab.countDistinctGuardianPositions());
        long now = System.currentTimeMillis();
        System.out.println("Solution for part two is: " + manufacturingLab.countPossibleObstructionPositions());
        System.out.println(System.currentTimeMillis() - now + "ms");
    }

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    static class Position {

        private final int x;
        private final int y;
        @Setter
        private boolean isObstruction;
    }

    record PositionMemory(int x, int y, Direction direction) {

        public static PositionMemory of(Position position, Direction direction) {
            return new PositionMemory(position.getX(), position.getY(), direction);
        }
    }

    enum Direction {
        UP, RIGHT, DOWN, LEFT;

        public Direction turnRight() {
            Direction[] directions = Direction.values();
            return directions[(this.ordinal() + 1) % directions.length];
        }
    }

    static class Guard {

        private final List<List<Position>> positionMap;
        @Getter
        private final Position startPosition;
        @Getter
        private Position position;
        private Position nextPosition;
        @Getter
        private Direction direction;

        public Guard(List<List<Position>> positionMap, Position position) {
            this(positionMap, position, UP);
        }

        public Guard(List<List<Position>> positionMap, Position position, Direction direction) {
            this.positionMap = positionMap;
            this.startPosition = position;
            this.position = position;
            this.direction = direction;
        }

        public Position peekNextPosition() {
            if (nextPosition != null) {
                return nextPosition;
            }
            int nextX = position.getX() + (direction == RIGHT ? 1 : (direction == LEFT ? -1 : 0));
            int nextY = position.getY() + (direction == DOWN ? 1 : (direction == UP ? -1 : 0));
            if (nextX < 0 || nextX >= positionMap.get(0).size() || nextY < 0 || nextY >= positionMap.size()) {
                return null;
            }
            nextPosition = positionMap.get(nextY).get(nextX);
            return nextPosition;
        }

        public void turnRight() {
            direction = direction.turnRight();
            nextPosition = null;
        }

        public Position walk() {
            position = peekNextPosition();
            nextPosition = null;
            return position;
        }

        public void reset() {
            this.position = startPosition;
            this.direction = UP;
        }

        public PositionMemory remember() {
            return PositionMemory.of(position, direction);
        }

        public static Guard copy(Guard guard) {
            return new Guard(guard.positionMap, guard.position, guard.direction);
        }
    }

    @Getter
    static class ManufacturingLab {

        private final List<List<Position>> positionMap;
        private final Guard guard;
        private final Set<Position> visitedPositions = new HashSet<>();
        private final Set<Position> possibleObstructions = new HashSet<>();

        ManufacturingLab(List<List<Position>> positionMap, Guard guard) {
            this.positionMap = positionMap;
            this.guard = guard;
            walkTheMap();
        }

        private void walkTheMap() {
            visitedPositions.add(guard.getPosition());
            Position nextPosition = guard.peekNextPosition();
            while (nextPosition != null) {
                if (nextPosition.isObstruction()) {
                    guard.turnRight();
                } else {
                    visitedPositions.add(guard.walk());
                }
                nextPosition = guard.peekNextPosition();
            }
            guard.reset();
        }

        public int countDistinctGuardianPositions() {
            return visitedPositions.size();
        }

        public int countPossibleObstructionPositions() {
            for (Position position : visitedPositions) {
                if (isPossibleObstruction(position)) {
                    possibleObstructions.add(position);
                }
            }
            return possibleObstructions.size();
        }

        private boolean isPossibleObstruction(Position position) {
            if (guard.getStartPosition().equals(position)) {
                return false;
            }
            
            Guard ghost = Guard.copy(guard);
            position.setObstruction(true);
            Set<PositionMemory> ghostMemories = new HashSet<>();
            
            while (ghost.peekNextPosition() != null) {
                if (ghostMemories.contains(ghost.remember())) {
                    position.setObstruction(false);
                    return true;
                }
                ghostMemories.add(ghost.remember());
                if (ghost.peekNextPosition().isObstruction()) {
                    ghost.turnRight();
                } else {
                    ghost.walk();
                }
            }
            
            position.setObstruction(false);
            return false;
        }
    }

    @UtilityClass
    static class InputParser {

        public static ManufacturingLab parse(List<String> input) {
            List<List<Position>> positionMap = new ArrayList<>();
            Guard guard = null;
            for (int y = 0; y < input.size(); y++) {
                List<Position> positions = new ArrayList<>();
                for (int x = 0; x < input.get(y).length(); x++) {
                    char character = input.get(y).charAt(x);
                    Position position = new Position(x, y, character == '#');
                    positions.add(position);
                    if (guard == null && character == '^') {
                        guard = new Guard(positionMap, position);
                    }
                }
                positionMap.add(positions);
            }
            assert guard != null;
            return new ManufacturingLab(positionMap, guard);
        }
    }
}
