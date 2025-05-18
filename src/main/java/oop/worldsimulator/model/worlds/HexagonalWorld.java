package oop.worldsimulator.model.worlds;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.organisms.animals.Human;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HexagonalWorld extends World {
    public HexagonalWorld(int width, int height) {
        super(width, height);
    }

    @Override
    public List<Position> getNeighbors(Position pos, int range) {
        int x = pos.getX();
        int y = pos.getY();
        List<Position> neighbors = new ArrayList<>();

        boolean evenRow = (y % 2 == 0);

        if (evenRow) {
            neighbors.add(new Position(x - range, y - range));
            neighbors.add(new Position(x, y - range));
            neighbors.add(new Position(x - range, y));
            neighbors.add(new Position(x + range, y));
            neighbors.add(new Position(x - range, y + range));
            neighbors.add(new Position(x, y + range));
        } else {
            neighbors.add(new Position(x, y - range));
            neighbors.add(new Position(x + range, y - range));
            neighbors.add(new Position(x - range, y));
            neighbors.add(new Position(x + range, y));
            neighbors.add(new Position(x, y + range));
            neighbors.add(new Position(x + range, y + range));
        }

        return neighbors;
    }

    @Override
    public Map<Human.Direction, Position> getValidMoves(Position position) {
        int x = position.getX();
        int y = position.getY();

        boolean evenRow = (y % 2 == 0);

        Map<Human.Direction, Position> allMoves = Map.of(
                Human.Direction.LEFT,       new Position(x - 1, y),
                Human.Direction.RIGHT,      new Position(x + 1, y),
                Human.Direction.UP_LEFT,    evenRow ? new Position(x - 1, y - 1) : new Position(x, y - 1),
                Human.Direction.UP_RIGHT,   evenRow ? new Position(x, y - 1)     : new Position(x + 1, y - 1),
                Human.Direction.DOWN_LEFT,  evenRow ? new Position(x - 1, y + 1) : new Position(x, y + 1),
                Human.Direction.DOWN_RIGHT, evenRow ? new Position(x, y + 1)     : new Position(x + 1, y + 1)
        );

        return allMoves.entrySet().stream()
                .filter(e -> positionWithinBounds(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}