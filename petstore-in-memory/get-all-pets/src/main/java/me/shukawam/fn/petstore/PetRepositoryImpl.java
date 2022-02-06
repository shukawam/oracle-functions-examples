package me.shukawam.fn.petstore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shukawam
 */
public class PetRepositoryImpl implements PetRepository {
    private List<Pet> petList = new ArrayList<>();

    public List<Pet> getPetList() {
        return petList;
    }

    public PetRepositoryImpl() {
        petList.add(new Pet(1, "dog"));
        petList.add(new Pet(2, "cat"));
        petList.add(new Pet(3, "bird"));
        petList.add(new Pet(4, "fish"));
        petList.add(new Pet(5, "snake"));
        petList.add(new Pet(6, "lizard"));
        petList.add(new Pet(7, "hamster"));
        petList.add(new Pet(8, "rabbit"));
        petList.add(new Pet(9, "turtle"));
        petList.add(new Pet(10, "pig"));
    }

    @Override
    public List<Pet> getAllPets() {
        return getPetList();
    }

    @Override
    public Pet getPetById(int id) {
        if (id > petList.size()) {
            return new Pet(0, "not found");
        }
        return getPetList().get(id - 1);
    }

    @Override
    public Pet createPet(Pet pet) {
        petList.add(pet);
        return pet;
    }
}
