package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.World;
import oop.worldsimulator.model.factory.OrganismFactory;
import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.organisms.Organism;

import java.util.Random;

public class Turtle extends Animal {
    private static final int STRENGTH = 2;
    private static final int INITIATIVE = 1;
    private static final String SYMBOL = "🐢";
    private static final String SPECIES = "Turtle";
    private static final boolean REGISTERED = register();


    private final Random random = new Random();


    public Turtle(int x, int y, World world) {
        super(STRENGTH, INITIATIVE, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    @Override
    public void action() {
        boolean shouldStay = random.nextInt(100) < 75;  // 75% chance to stay still

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


    private static boolean register() {
        OrganismFactory.getInstance().registerType(SPECIES, Turtle::new);
        return true;
    }
}