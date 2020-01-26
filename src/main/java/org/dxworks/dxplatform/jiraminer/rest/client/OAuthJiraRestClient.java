package org.dxworks.dxplatform.jiraminer.rest.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfiguration;

import java.util.Map;

@Slf4j
public class OAuthJiraRestClient implements JiraRestClient {

    public static final String NOT_YET_IMPLEMENTED_MESSAGE = "Oauth is not yet supported, please use one of the other authentication types!";

    public OAuthJiraRestClient(JiraMinerConfiguration jiraMinerConfiguration) {
        log.error(NOT_YET_IMPLEMENTED_MESSAGE);
        throw new NotImplementedException(NOT_YET_IMPLEMENTED_MESSAGE);
    }

    @Override
    public <T> T getRequest(String jiraUrl, Map<String, String> headers, Class<T> responseType) {
        return null;
    }

    @Override
    public <T> T postRequest(String jiraUrl, Map<String, String> headers, Object body, Class<T> responseType) {
        return null;
    }
}
