![JUnit Utils](JUtils-06.svg){width=251 height=156}

# junit-json-source
JUnit 5 support for JSON Parameterized Test Sources


## 1. @JsonFileSource
@JsonFileSource takes in a list of JSON files that contain test data. 

```java
public class TestJsonFileArgumentProvider {
    @JsonFileSource(resources = {"test.json"})
    @ParameterizedTest
    void test(BigDecimal actual, BigDecimal expected) {
        Assertions.assertEquals(expected, actual);
    }
}
```

On the test above, test.json is a file that contains test data. The structure of the test data is shown below.

````json
[
  [
    {
      "value": "1",
      "type": "java.math.BigDecimal"
    },
    {
      "value": "1",
      "type": "java.math.BigDecimal"
    }
  ],
  [
    {
      "value": "2",
      "type": "java.math.BigDecimal"
    },
    {
      "value": "2",
      "type": "java.math.BigDecimal"
    }
  ]

]
````

The JSON shows a list of test cases. The first test case is a list with two objects, one with the actual value and the other with the expected value.

The structure is as follows:

* Test case:
    ```json
    [
        {
          "value": "1",
          "type": "java.math.BigDecimal"
        },
        {
          "value": "1",
          "type": "java.math.BigDecimal"
        }
      ]
    ```

* Actual Value:
    ```json
    {
      "value": "1",
      "type": "java.math.BigDecimal"
    }
    ```

* Expected Value:
    ```json
    {
      "value": "1",
      "type": "java.math.BigDecimal"
    }
    ```

NB: In these objects, you have to specify the type and value.

The test.json can separated into 2 JSON files, with each file containing one test case. 
One of the file will look like what is shown below.

```json
[
  [
    {
      "value": "1",
      "type": "java.math.BigDecimal"
    },
    {
      "value": "1",
      "type": "java.math.BigDecimal"
    }
  ]
]
```

Here is how the list of resources will look like:

```java
public class TestJsonFileArgumentProvider {
    @JsonFileSource(resources = {"test1.json", "test2.json"})
    @ParameterizedTest
    void test(BigDecimal actual, BigDecimal expected) {
        Assertions.assertEquals(expected, actual);
    }
}
```

## 2. @JsonSource
JsonSource accepts JSON as an argument for a test.

The example below shows a JSON structure for when you have <u>one argument</u> for your test method.
```java
public class TestJsonFileArgumentProvider {
    @JsonSource(
            value = "{" +
                    "    'type': java.math.BigDecimal, " +
                    "    'value': '1'" +
                    "}"
    )
    @ParameterizedTest
    void test(BigDecimal actual) {
        Assertions.assertNotNull(actual);
    }
}
```

The example below shows a JSON structure for when you have <u>two arguments</u> for your test method.
```java
public class TestJsonFileArgumentProvider {
    @JsonSource(
            value = "[" +
                    "{" +
                    "    'type': java.math.BigDecimal, " +
                    "    'value': '1'" +
                    "}," +
                    "{" +
                    "    'type': java.math.BigDecimal, " +
                    "    'value': '1'" +
                    "}" +
                    "]"
    )
    @ParameterizedTest
    void test(BigDecimal actual, BigDecimal expected) {
        Assertions.assertEquals(expected, actual);
    }
}
```

## 3. @EnabledIfReachable
@EnabledIfReachable enables the test if the URL is reachable, and disables it otherwise.

```java
public class TestJsonFileArgumentProvider {
    @Test
    @EnabledIfReachable(
            url = "http://google.com/",
            timeoutMillis = 1000)
    void reachableUrl() { }

    @Test
    @EnabledIfReachable(
            url = "http://com.google/",
            timeoutMillis = 1000)
    void unreachableUrl() { }
}
```



