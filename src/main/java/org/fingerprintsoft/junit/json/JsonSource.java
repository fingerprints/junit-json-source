package org.fingerprintsoft.junit.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Accepts JSON as an argument for a test, the Json must be in the following format
 * <ul>
 *     <li>
 *         For a single argument e.g. mytestMethod(BigDecimal one)
 *         <code>
 *             {
 *               "value": "1",
 *               "type": "java.math.BigDecimal"
 *             }
 *         </code>
 *     </li>
 *     <li>
 *         For a multiple arguments e.g. mytestMethod(BigDecimal, one, String, two)
 *         <code>
 *          [
 *            {
 *              "value": "1",
 *              "type": "java.math.BigDecimal"
 *            },
 *            {
 *              "value": "1",
 *              "type": "java.lang.String"
 *            }
 *          ]
 *         </code>
 *     </li>
 * </ul>
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(JsonArgumentsProvider.class)
public @interface JsonSource {

    String[] value();

}
