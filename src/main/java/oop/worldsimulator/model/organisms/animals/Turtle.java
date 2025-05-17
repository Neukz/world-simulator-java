package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.World;
import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.organisms.Organism;

public class Turtle extends Animal {
    public static final String SPECIES = "Turtle";

    private static final int STRENGTH = 2;
    private static final int INITIATIVE = 1;
    private static final String SYMBOL = "🐢";


    public Turtle(int x, int y, World world) {
        super(STRENGTH, INITIATIVE, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    @Override
    public void action() {
        boolean shouldStay = RANDOM.nextInt(100) < 75;  // 75% chance to stay still

        if (shouldStay) {
            return;
        }

        super.action();
    }

    @Override
    public void collision(Organism other) {
        // If the attacker is a different species and weak, deflect
        if (!(other instanceof Turtle) && other.getStrength() < 5) {
            deflectAttack(other);
            return;
        }

        super.collision(other);
    }

    private void deflectAttack(Organism attacker) {
        Position attackerPrevPos = attacker.getPrevPosition();
        attacker.setPosition(attackerPrevPos);
    }
}