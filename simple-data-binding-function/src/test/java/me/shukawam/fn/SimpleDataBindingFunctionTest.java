package me.shukawam.fn;

import com.fnproject.fn.testing.*;
import org.junit.*;

import static org.junit.Assert.*;

public class SimpleDataBindingFunctionTest {
    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void testSimpleDataBinding_withName() {
        testing.givenEvent()
                .withBody("john")
                .enqueue();
        testing.thenRun(SimpleDataBindingFunction.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals("Hello john!", result.getBodyAsString());
    }

    @Test
    public void testSimpleDataBinding_noName() {
        testing.givenEvent().enqueue();
        testing.thenRun(SimpleDataBindingFunction.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals("Hello world!", result.getBodyAsString());
    }

}
