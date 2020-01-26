package org.dxworks.dxplatform.jiraminer.rest.client;

import org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfiguration;
import org.springframework.http.HttpHeaders;

import static org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfigValidation.notNull;

public class CookieAuthenticationJiraRestClient extends NoAuthJiraRestClient {

    private static final String JIRA_COOKIE_FIELD = "cookie";

    private String cookie;

    public CookieAuthenticationJiraRestClient(JiraMinerConfiguration jiraMinerConfiguration) {
        super();
        cookie = jiraMinerConfiguration.getProperty(JIRA_COOKIE_FIELD);
        notNull(cookie, "Cookie not provided!");

        setAuthenticationHeaders(basicHttpHeaders);
    }

    @Override
    protected void setAuthenticationHeaders(HttpHeaders httpHeaders) {
        httpHeaders.set("Cookie", cookie);
    }
}
