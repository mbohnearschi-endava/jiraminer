package org.dxworks.dxplatform.jiraminer.rest.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
public class NoAuthJiraRestClient implements JiraRestClient {

    protected RestTemplate restTemplate = new RestTemplate();
    protected HttpHeaders basicHttpHeaders;

    public NoAuthJiraRestClient() {
        basicHttpHeaders = createBasicHttpHeaders();
    }

    @Override
    public <T> T getRequest(String restUrl, Map<String, String> headers, Class<T> responseType) {
        try {
            return restTemplate.exchange(restUrl, HttpMethod.GET,
                    new HttpEntity<>(createTemporaryHeaders(headers)), responseType)
                    .getBody();
        } catch (HttpClientErrorException e) {
            if (e instanceof HttpClientErrorException.Unauthorized)
                throw new JiraMinerUnauthorizedException(e);
            log.error("Could not validate project! ");
            return null;
        }
    }

    @Override
    public <T> T postRequest(String jiraUrl, Map<String, String> headers, Object body, Class<T> responseType) {
        return restTemplate.exchange(jiraUrl, HttpMethod.POST,
                new HttpEntity<>(body, createTemporaryHeaders(headers)), responseType)
                .getBody();
    }

    private HttpHeaders createTemporaryHeaders(Map<String, String> extraHeaders) {
        if (CollectionUtils.isEmpty(extraHeaders))
            return basicHttpHeaders;

        HttpHeaders httpHeaders = createBasicHttpHeaders();
        httpHeaders.setAll(extraHeaders);
        return httpHeaders;
    }


    private HttpHeaders createBasicHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", "application/json");
        setAuthenticationHeaders(httpHeaders);
        return httpHeaders;
    }

    protected void setAuthenticationHeaders(HttpHeaders httpHeaders) {
        //Intentionally left empty. No authentication is required
    }
}
