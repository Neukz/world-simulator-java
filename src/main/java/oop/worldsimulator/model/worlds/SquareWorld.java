package oop.worldsimulator.model.worlds;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.organisms.animals.Human;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SquareWorld extends World {
    public SquareWorld(int width, int height) {
        super(width, height);
    }

    @Override
    public List<Position> getNeighbors(Position position, int range) {
        int x = position.getX();
        int y = position.getY();
        List<Position> neighbors = new ArrayList<>();

        neighbors.add(new Position(x, y - range));
        neighbors.add(new Position(x, y + range));
        neighbors.add(new Position(x - range, y));
        neighbors.add(new Position(x + range, y));

        return neighbors;
    }

    @Override
    public Map<Human.Direction, Position> getValidMoves(Position position) {
        int x = position.getX();
        int y = position.getY();

        Map<Human.Direction, Position> allMoves = Map.of(
                Human.Direction.UP,    new Position(x, y - 1),
                Human.Direction.DOWN,  new Position(x, y + 1),
                Human.Direction.LEFT,  new Position(x - 1, y),
                Human.Direction.RIGHT, new Position(x + 1, y)
        );

        return allMoves.entrySet().stream()
                .filter(e -> positionWithinBounds(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}