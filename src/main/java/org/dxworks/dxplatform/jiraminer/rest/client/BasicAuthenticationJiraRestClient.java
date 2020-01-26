package org.dxworks.dxplatform.jiraminer.rest.client;

import org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfiguration;
import org.springframework.http.HttpHeaders;

import java.util.Base64;

import static org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfigValidation.notNull;


public class BasicAuthenticationJiraRestClient extends NoAuthJiraRestClient {

    private static final String JIRA_PASSWORD_FIELD = "password";
    private static final String JIRA_USERNAME_FIELD = "username";

    private String username;
    private String password;

    public BasicAuthenticationJiraRestClient(JiraMinerConfiguration jiraMinerConfiguration) {
        super();
        username = jiraMinerConfiguration.getProperty(JIRA_USERNAME_FIELD);
        notNull(username, "Username not provided!");

        password = jiraMinerConfiguration.getProperty(JIRA_PASSWORD_FIELD);
        notNull(password, "Password not provided!");

        setAuthenticationHeaders(basicHttpHeaders);
    }

    @Override
    protected void setAuthenticationHeaders(HttpHeaders httpHeaders) {
        httpHeaders.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
    }
}
