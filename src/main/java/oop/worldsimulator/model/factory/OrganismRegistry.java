package oop.worldsimulator.model.factory;

import oop.worldsimulator.model.organisms.animals.*;
import oop.worldsimulator.model.organisms.plants.*;

public final class OrganismRegistry {
    public static void registerAll() {
        OrganismFactory factory = OrganismFactory.getInstance();

        // Animals
        factory.registerType(Human.SPECIES, Human::spawn);
        factory.registerType(Wolf.SPECIES, Wolf::new);
        factory.registerType(Sheep.SPECIES, Sheep::new);
        factory.registerType(Fox.SPECIES, Fox::new);
        factory.registerType(Turtle.SPECIES, Turtle::new);
        factory.registerType(Antelope.SPECIES, Antelope::new);

        // Plants
        factory.registerType(Grass.SPECIES, Grass::new);
        factory.registerType(SowThistle.SPECIES, SowThistle::new);
        factory.registerType(Guarana.SPECIES, Guarana::new);
        factory.registerType(Belladonna.SPECIES, Belladonna::new);
        factory.registerType(SosnowskysHogweed.SPECIES, SosnowskysHogweed::new);
    }


    private OrganismRegistry() {}
}