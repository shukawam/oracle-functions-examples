package me.shukawam.fn;

public class SimpleDataBindingFunction {
    public String handleRequest(String input) {
        var name = (input == null || input.isEmpty()) ? "world" : input;
        return "Hello " + name + "!";
    }
}
