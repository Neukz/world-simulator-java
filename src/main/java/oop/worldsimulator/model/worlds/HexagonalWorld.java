package oop.worldsimulator.model.worlds;

import oop.worldsimulator.model.Position;

import java.util.ArrayList;
import java.util.List;

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
}