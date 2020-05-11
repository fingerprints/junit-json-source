package org.fingerprintsoft.junit.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Accepts JSON file as an argument for a test, the Json must be in the following format
 * <ul>
 *     <li>
 *         For a single argument e.g. mytestMethod(BigDecimal one)
 *         <code>
 *             [
 *               {
 *                 "value": "1",
 *                 "type": "java.math.BigDecimal"
 *               }
 *             ]
 *         </code>
 *     </li>
 *     <li>
 *         For a multiple arguments e.g. mytestMethod(BigDecimal, one, String, two)
 *         <code>
 *            [
 *              [
 *                {
 *                  "value": "1",
 *                  "type": "java.math.BigDecimal"
 *                },
 *                {
 *                  "value": "1",
 *                  "type": "java.lang.String"
 *                }
 *              ]
 *            ]
 *         </code>
 *     </li>
 *     <li>
 *         For a multiple tests, just include multiple objects in the top most array, examples
 *         <code>
 *            [
 *              {
 *                "value": "1",
 *                "type": "java.math.BigDecimal"
 *              },
 *              {
 *                "value": "2",
 *                "type": "java.math.BigDecimal"
 *              },
 *            ]
 *         </code>
 *         <code>
 *            [
 *              [
 *                {
 *                  "value": "1",
 *                  "type": "java.math.BigDecimal"
 *                },
 *                {
 *                  "value": "1",
 *                  "type": "java.lang.String"
 *                }
 *              ],
 *              [
 *                {
 *                  "value": "2",
 *                  "type": "java.math.BigDecimal"
 *                },
 *                {
 *                  "value": "2",
 *                  "type": "java.lang.String"
 *                }
 *              ],
 *            ]
 *         </code> *     </li>
 * </ul>
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(JsonFileArgumentsProvider.class)
public @interface JsonFileSource {

    /**
     * The JSON classpath resources to use as the sources of arguments; must not be
     * empty.
     * @return list of file resource names
     */
    String[] resources();

    /**
     * The encoding to use when reading the JSON files; must be a valid charset.
     *
     * <p>Defaults to {@code "UTF-8"}.
     *
     * @return the file encoding
     * @see java.nio.charset.StandardCharsets
     */
    String encoding() default "UTF-8";

}
