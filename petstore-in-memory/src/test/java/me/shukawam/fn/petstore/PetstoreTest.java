package me.shukawam.fn.petstore;

import com.fnproject.fn.api.OutputEvent;
import com.fnproject.fn.testing.FnResult;
import com.fnproject.fn.testing.FnTestingRule;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.*;

/**
 * @author shukawam
 */
public class PetstoreTest {
    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void testGetAllPets() {
        testing.givenEvent()
                .withHeader("Fn-Http-Method", "GET")
                .withHeader("Fn-Http-Request-Url", "/api/pets")
                .enqueue();
        testing.thenRun(Petstore.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals(OutputEvent.Status.Success, result.getStatus());
        assertEquals("[{\"id\":1,\"name\":\"dog\"},{\"id\":2,\"name\":\"cat\"},{\"id\":3,\"name\":\"bird\"},{\"id\":4,\"name\":\"fish\"},{\"id\":5,\"name\":\"snake\"},{\"id\":6,\"name\":\"lizard\"},{\"id\":7,\"name\":\"hamster\"},{\"id\":8,\"name\":\"rabbit\"},{\"id\":9,\"name\":\"turtle\"},{\"id\":10,\"name\":\"pig\"}]"
                , result.getBodyAsString());
    }

    @Test
    public void testGetPetById() {
        testing.givenEvent()
                .withHeader("Fn-Http-Method", "GET")
                .withHeader("Fn-Http-Request-Url", "/api/pets/1")
                .enqueue();
        testing.thenRun(Petstore.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals(OutputEvent.Status.Success, result.getStatus());
        assertEquals("{\"id\":1,\"name\":\"dog\"}", result.getBodyAsString());
    }

    @Test
    public void testGetPetById_NotFound() {
        testing.givenEvent()
                .withHeader("Fn-Http-Method", "GET")
                .withHeader("Fn-Http-Request-Url", "/api/pets/100")
                .enqueue();
        testing.thenRun(Petstore.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals(OutputEvent.Status.Success, result.getStatus());
        assertEquals("{\"id\":0,\"name\":\"not found\"}", result.getBodyAsString());
    }

    @Test
    public void testCreatePet() throws JSONException {
        int id = 11;
        String name = "bird";
        testing.givenEvent()
                .withHeader("Fn-Http-Method", "POST")
                .withHeader("Fn-Http-Request-Url", "/api/pets")
                .withBody(String.format("{\"id\":%d,\"name\":\"%s\"}", id, name))
                .enqueue();
        testing.thenRun(Petstore.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals(OutputEvent.Status.Success, result.getStatus());
        JSONAssert.assertEquals(String.format("{\"id\":%d,\"name\":\"%s\"}", id, name), result.getBodyAsString(), true);
    }

    @Test
    public void testMethodNotAllowed() {
        testing.givenEvent()
                .withHeader("Fn-Http-Method", "PUT")
                .withHeader("Fn-Http-Request-Url", "/api/pets/1")
                .enqueue();
        testing.thenRun(Petstore.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals(OutputEvent.Status.Success, result.getStatus());
        assertEquals("{\"message\":\"error\",\"data\":\"Method not supported\"}", result.getBodyAsString());
    }
}
