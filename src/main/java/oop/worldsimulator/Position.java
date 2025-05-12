package oop.worldsimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Position implements Comparable<Position> {
    private static final Random RANDOM = new Random();

    public static final Position INVALID_POSITION = new Position(-1, -1);


    private int x, y;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Position> getNeighbors(int range) {
        List<Position> neighbors = new ArrayList<>();

        neighbors.add(new Position(x, y - range));
        neighbors.add(new Position(x, y + range));
        neighbors.add(new Position(x - range, y));
        neighbors.add(new Position(x + range, y));

        return neighbors;
    }

    public Position getRandomNeighbor(int range) {
        List<Position> neighbors = getNeighbors(range);
        return neighbors.get(RANDOM.nextInt(neighbors.size()));
    }

    @Override
    public int compareTo(Position other) {
        if (this.y == other.y) {
            return Integer.compare(this.x, other.x);    // If y is the same, smaller x is smaller position
        }
        return Integer.compare(this.y, other.y);    // Smaller y is smaller position
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position other) {
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}