package me.shukawam.fn.petstore;

import java.util.List;

/**
 * A repository interface for pets.
 *
 * @author shukawam
 */
public interface PetRepository {
    List<Pet> getAllPets();
    Pet getPetById(int id);
    Pet createPet(Pet pet);
}
