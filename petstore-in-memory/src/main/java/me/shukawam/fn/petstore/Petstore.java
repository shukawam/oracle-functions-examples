package me.shukawam.fn.petstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnproject.fn.api.InputEvent;
import com.fnproject.fn.api.httpgateway.HTTPGatewayContext;

import java.io.IOException;
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

    public Object handleRequest(HTTPGatewayContext ctx, InputEvent inputEvent) {
        if (ctx.getMethod().equals("GET")) {
            return handleGetRequest(ctx);
        } else if (ctx.getMethod().equals("POST")) {
            return handlePostRequest(ctx, inputEvent);
        } else {
            return new Response("error", "Method not supported");
        }
    }

    private Object handleGetRequest(HTTPGatewayContext ctx) {
        LOGGER.info(ctx.getRequestURL());
        if (ctx.getRequestURL().equals("/api/pets")) {
            LOGGER.info("GET /api/pets");
            return getAllPets(new PetRepositoryImpl());
        } else {
            LOGGER.info("GET /api/pets/{id}");
            return getPetById(Integer.parseInt(ctx.getRequestURL().substring(ctx.getRequestURL().lastIndexOf("/") + 1)), petRepository);
        }
    }

    public Object handlePostRequest(HTTPGatewayContext ctx, InputEvent inputEvent) {
        LOGGER.info(ctx.getRequestURL());
        Pet pet = inputEvent.consumeBody(inputStream -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(inputStream, Pet.class);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.info("Error parsing request body");
                return null;
            }
        });
        if (ctx.getRequestURL().equals("/api/pets")) {
            return createPet(pet, petRepository);
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
