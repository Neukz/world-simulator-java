package oop.worldsimulator.model;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Comparable<Position>, Serializable {
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