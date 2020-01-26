package org.dxworks.dxplatform.jiraminer.rest.client;

import org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfiguration;

public class JiraRestClientFactory {

    private JiraMinerConfiguration jiraMinerConfiguration;

    public JiraRestClientFactory(JiraMinerConfiguration jiraMinerConfiguration) {
        this.jiraMinerConfiguration = jiraMinerConfiguration;
    }

    public JiraRestClient createByAuthenticationType() {
        switch (jiraMinerConfiguration.getAuthenticationType()) {
            case BASIC:
                return new BasicAuthenticationJiraRestClient(jiraMinerConfiguration);
            case COOKIE:
                return new CookieAuthenticationJiraRestClient(jiraMinerConfiguration);
            case OAUTH:
                return new OAuthJiraRestClient(jiraMinerConfiguration);
            default:
                return new NoAuthJiraRestClient();
        }
    }
}
