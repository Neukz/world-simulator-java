package oop.worldsimulator.model.worlds;

import oop.worldsimulator.model.Position;

import java.util.ArrayList;
import java.util.List;

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
}