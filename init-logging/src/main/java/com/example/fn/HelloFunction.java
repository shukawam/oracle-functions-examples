package com.example.fn;

import com.fnproject.fn.api.FnConfiguration;
import com.fnproject.fn.api.RuntimeContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class HelloFunction {
    private static final Logger LOGGER = Logger.getLogger(HelloFunction.class.getName());

    @FnConfiguration
    public void setUp(RuntimeContext context) {
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        LOGGER.info(sf.format(now) + ": HelloFunction setUp");
    }

    public String handleRequest(String input) {
        String name = (input == null || input.isEmpty()) ? "world"  : input;

        System.out.println("Inside Java Hello World function"); 
        return "Hello, " + name + "!";
    }

}