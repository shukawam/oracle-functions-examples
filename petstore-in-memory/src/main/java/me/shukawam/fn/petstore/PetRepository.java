package me.shukawam.fn.petstore;

import java.util.List;

/**
 * @author shukawam
 */
public interface PetRepository {
    List<Pet> getAllPets();
    Pet getPetById(int id);
    Pet createPet(Pet pet);
}
