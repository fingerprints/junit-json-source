import java.math.BigDecimal;

import org.fingerprintsoft.junit.json.JsonFileSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;

public class TestJsonFileArgumentProvider {


    @JsonFileSource(resources = {"test.json"})
    @ParameterizedTest
    void test(BigDecimal actual, BigDecimal expected) {
        Assertions.assertEquals(expected, actual);
    }

    @JsonFileSource(resources = {"test-one-arg.json"})
    @ParameterizedTest
    void test(BigDecimal actual) {
        Assertions.assertNotNull(actual);
    }
}
