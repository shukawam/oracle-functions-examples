package me.shukawam.fn.petstore;

/**
 * @author shukawam
 */
public class Response {
    public String message;
    public Object data;

    public Response(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
