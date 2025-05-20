package oop.worldsimulator.model.factory;

import oop.worldsimulator.model.worlds.World;
import oop.worldsimulator.model.organisms.Organism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OrganismFactory {
    @FunctionalInterface
    public interface OrganismCreator {
        Organism create(int x, int y, World world);
    }


    // Singleton instance
    private static final OrganismFactory INSTANCE = new OrganismFactory();


    private final Map<String, OrganismCreator> creators = new HashMap<>();


    private OrganismFactory() {}

    public void registerType(String name, OrganismCreator creator) {
        creators.put(name, creator);
    }

    public Organism create(String name, int x, int y, World world) {
        OrganismCreator creator = creators.get(name);

        if (creator != null) {
            return creator.create(x, y, world);
        }

        return null;
    }

    public List<String> getRegisteredTypes() {
        return new ArrayList<>(creators.keySet());
    }


    public static OrganismFactory getInstance() {
        return INSTANCE;
    }
}