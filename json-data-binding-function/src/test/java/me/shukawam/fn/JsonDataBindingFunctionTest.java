package me.shukawam.fn;

import com.fnproject.fn.testing.FnResult;
import com.fnproject.fn.testing.FnTestingRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author shukawam
 */
public class JsonDataBindingFunctionTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void testJsonDataBindingFunction() {
        testing.givenEvent().enqueue();
        testing.thenRun(JsonDataBindingFunction.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals("{\"name\":\"\",\"salutation\":\"Hello\"}", result.getBodyAsString());
    }
}
