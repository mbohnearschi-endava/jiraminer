package org.dxworks.dxplatform.jiraminer.rest.client;

import java.util.Map;

public interface JiraRestClient {
    <T> T getRequest(String jiraUrl, Map<String, String> headers, Class<T> responseType);

    <T> T postRequest(String jiraUrl, Map<String, String> headers, Object body, Class<T> responseType);
}
