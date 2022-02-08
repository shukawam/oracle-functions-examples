package me.shukawam.fn.petstore;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * A pet.
 *
 * @author shukawam
 */
public class Pet {
    public int id;
    public String name;

    @JsonCreator
    public Pet(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
