package me.shukawam.fn.petstore;

import com.fnproject.fn.api.InputEvent;
import com.fnproject.fn.api.httpgateway.HTTPGatewayContext;

import java.util.List;
import java.util.logging.Logger;

/**
 * A handler function for the petstore API.
 *
 * @author shukawam
 */
public class Petstore {
    private static final Logger LOGGER = Logger.getLogger(Petstore.class.getName());
    private static final PetRepositoryImpl petRepository = new PetRepositoryImpl();

    public Object handleGetRequest(HTTPGatewayContext ctx) {
        if (ctx.getMethod().equals("GET")) {
            LOGGER.info(ctx.getRequestURL());
            if (ctx.getRequestURL().equals("/api/pets")) {
                LOGGER.info("GET /api/pets");
                return getAllPets(new PetRepositoryImpl());
            } else {
                LOGGER.info("GET /api/pets/{id}");
                return getPetById(Integer.parseInt(ctx.getRequestURL().substring(ctx.getRequestURL().lastIndexOf("/") + 1)), petRepository);
            }
        } else {
            return new Response("error", "Method not supported");
        }
    }

    public Object handlePostRequest(Pet pet, HTTPGatewayContext ctx) {
        if (ctx.getMethod().equals("POST")) {
            LOGGER.info(ctx.getRequestURL());
            if (ctx.getRequestURL().equals("/api/pets")) {
                return createPet(pet, petRepository);
            } else {
                return new Response("error", "Method not supported");
            }
        } else {
            return new Response("error", "Method not supported");
        }
    }

    private List<Pet> getAllPets(PetRepositoryImpl petRepository) {
        return petRepository.getAllPets();
    }

    private Pet getPetById(int id, PetRepositoryImpl petRepository) {
        return petRepository.getPetById(id);
    }

    private Pet createPet(Pet pet, PetRepositoryImpl petRepository) {
        return petRepository.createPet(pet);
    }
}
