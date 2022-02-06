package me.shukawam.fn;

import com.fnproject.fn.testing.FnResult;
import com.fnproject.fn.testing.FnTestingRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author shukawam
 */
public class HttpGatewayFunctionTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void testHttpGatewayFunction() {
        testing.givenEvent()
                .withHeader("Fn-Http-Request-Url", "https://www.example.com/http-gateway-context")
                .withHeader("Fn-Http-Method", "GET")
                .enqueue();
        testing.thenRun(HttpGatewayFunction.class, "handleRequest");
        FnResult result = testing.getOnlyResult();
        assertEquals("Hello, World", result.getBodyAsString());
    }
}
