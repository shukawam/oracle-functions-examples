package example.micronaut;
import com.fnproject.fn.testing.FnTestingRule;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FunctionTest {
    @Test
    public void testFunction() {
        FnTestingRule rule = FnTestingRule.createDefault();
        rule.givenEvent().enqueue();
        rule.thenRun(Function.class, "handleRequest");
        String result = rule.getOnlyResult().getBodyAsString();
        assertNotNull(result);
    }
}
