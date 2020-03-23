package org.fingerprintsoft.junit.enabled;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import static java.lang.String.format;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

class EnabledIfReachableCondition implements ExecutionCondition {

    private static final ConditionEvaluationResult ENABLED_BY_DEFAULT =
            enabled("@EnabledIfReachable is not present");

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(
            ExtensionContext context) {
        AnnotatedElement element = context
                .getElement()
                .orElseThrow(IllegalStateException::new);
        return findAnnotation(element, EnabledIfReachable.class)
                .map(annotation -> disableIfUnreachable(annotation, element))
                .orElse(ENABLED_BY_DEFAULT);
    }

    private ConditionEvaluationResult disableIfUnreachable(
            EnabledIfReachable annotation, AnnotatedElement element) {
        String url = annotation.url();
        int timeoutMillis = annotation.timeoutMillis();
        boolean reachable = pingURL(url, timeoutMillis);
        if (reachable)
            return enabled(format(
                    "%s is enabled because %s is reachable",
                    element, url));
        else
            return disabled(format(
                    "%s is disabled because %s could not be reached in %dms",
                    element, url, timeoutMillis));
    }

    /**
     * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in
     * the 200-399 range.
     *
     * @param url     The HTTP URL to be pinged.
     * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
     *                the total timeout is effectively two times the given timeout.
     * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
     * given timeout, otherwise <code>false</code>.
     */
    public static boolean pingURL(String url, int timeout) {
        url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }

}
