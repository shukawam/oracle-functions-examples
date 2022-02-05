package me.shukawam.fn;

import com.fnproject.fn.testing.*;
import org.junit.*;

import static org.junit.Assert.*;

public class SimpleDataBindingFunction {
    @Rule
    private static final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void testSimpleDataBinding() {
        testing.givenEvent().enqueue();
        testing.thenRun(SimpleDataBindingFunction.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals("Hello world!", result.getBodyAsString());
    }

}
