package me.shukawam.fn;

/**
 * @author shukawam
 */
public class JsonDataBindingFunction {

    public Greeting handleRequest(String name) {
        return new Greeting(name, "Hello");
    }
}
