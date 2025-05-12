package oop.worldsimulator.organisms;

import oop.worldsimulator.Position;
import oop.worldsimulator.World;

import java.util.Comparator;

public abstract class Organism implements Comparable<Organism> {
    public static final Comparator<Organism> COMPARE_BY_POSITION = Comparator.comparing(o -> o.position);


    private int age = 0;
    private boolean alive = true;
    private int strength;
    private int initiative;
    private String symbol;
    private Position position;
    private Position prevPosition;

    protected World world;


    public Organism(int strength, int initiative, String symbol, int x, int y, World world) {
        this.strength = strength;
        this.initiative = initiative;
        this.symbol = symbol;
        this.position = new Position(x, y);
        this.prevPosition = new Position(x, y);
        this.world = world;
    }

    public int getAge() {
        return age;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getStrength() {
        return strength;
    }

    public String getSymbol() {
        return symbol;
    }

    public Position getPosition() {
        return position;
    }

    public Position getPrevPosition() {
        return prevPosition;
    }

    public void mature() {
        age++;
    }

    public void kill() {
        this.alive = false;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setPosition(Position newPosition) {
        if (world.positionWithinBounds(newPosition)) {
            this.position = newPosition;
        }
    }

    public void setPrevPosition(Position newPosition) {
        this.prevPosition = newPosition;
    }

    @Override
    public int compareTo(Organism other) {
        if (this.initiative == other.initiative) {
            return Integer.compare(other.age, this.age);    // If initiative is the same, older is higher priority
        }
        return Integer.compare(other.initiative, this.initiative);  // Higher initiative is higher priority
    }

    public abstract void action();
    public abstract void collision(Organism other);

    protected void reproduce() {
        // TODO
    }
}