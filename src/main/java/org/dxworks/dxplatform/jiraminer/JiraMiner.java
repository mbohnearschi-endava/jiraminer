package org.dxworks.dxplatform.jiraminer;

import lombok.extern.slf4j.Slf4j;
import org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfiguration;
import org.dxworks.dxplatform.jiraminer.rest.client.JiraRestClient;
import org.dxworks.dxplatform.jiraminer.rest.response.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfigurationFields.API_BASE;
import static org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfigurationFields.FIELDS;

@Slf4j
@Service
public class JiraMiner {

    private JiraRestClient restClient;
    private JiraMinerConfiguration jiraMinerConfiguration;

    public JiraMiner(JiraRestClient restClient, JiraMinerConfiguration jiraMinerConfiguration) {
        this.restClient = restClient;
        this.jiraMinerConfiguration = jiraMinerConfiguration;
    }

    public List<JiraIssueDTO> getIssues() {
        String jiraHome = jiraMinerConfiguration.getJiraHome();

        List<String> existingJiraProjects = filterOnlyExistingProjects(jiraMinerConfiguration.getProjects());
        String jql = createJqlQuery(existingJiraProjects);

        log.info("Created jql query: '{}'", jql);

        List<Issue> allIssues = new ArrayList<>();

        String jiraJqlLink = jiraHome + API_BASE + "search";

        int startAt = 0;
        int maxResults = 500;
        int total;

        do {
            IssueSearchResult searchResult = searchIssues(jiraJqlLink, jql, maxResults, startAt);


            allIssues.addAll(searchResult.getIssues());

            total = searchResult.getTotal();
            startAt = startAt + maxResults;
            log.info("Got issues ({}/{})", Math.min(startAt, total), total);
        } while (startAt < total);

        return createJiraIssueDTOSFromIssues(allIssues);
    }

    private List<String> filterOnlyExistingProjects(List<String> jiraProjects) {
        return jiraProjects.stream().map(String::trim)
                .filter(this::validateJiraProject)
                .collect(Collectors.toList());
    }

    private synchronized boolean validateJiraProject(String projectID) {

        final ProjectValidateDTO body = restClient.getRequest(getProjectValidationUrl(projectID), null, ProjectValidateDTO.class);
        if (body != null) {
            final ProjectValidateErrorsDTO errors = body.getErrors();
            if (errors != null) {
                return errors.getProjectKey().matches("Project '[^']*' uses this project key\\.");
            }
        }
        log.error("Project with id " + projectID + " does not exist on host " + jiraMinerConfiguration.getJiraHome());
        return false;
    }

    private String getProjectValidationUrl(String projectID) {
        return jiraMinerConfiguration.getJiraHome() + API_BASE + "projectvalidate/key?key=" + projectID;
    }

    private String createJqlQuery(List<String> existingJiraProjects) {
        String jql = "project in (";
        jql += existingJiraProjects.stream()
                .map(this::encloseInQuotes)
                .collect(Collectors.joining(","));
        jql += ")";

        return jql;
    }

    private String encloseInQuotes(String s) {
        return "\"" + s + "\"";
    }

    private synchronized IssueSearchResult searchIssues(String jiraUrl, String jql, int maxResults, int startAt) {
        return restClient.postRequest(jiraUrl, null,
                new JiraIssuesRequestBodyDTO(jql, startAt, maxResults, FIELDS), IssueSearchResult.class);
    }

    private List<JiraIssueDTO> createJiraIssueDTOSFromIssues(Collection<Issue> allIssues) {
        return allIssues.stream().map(issue -> {
            final IssueFields fields = issue.getFields();
            return JiraIssueDTO.builder().key(issue.getKey()).issueType(fields.getIssuetype().getName())
                    .status(fields.getStatus().getName()).startDate(fields.getCreated()).endDate(fields.getUpdated())
                    .parentKey(getParentKey(issue)).summary(fields.getSummary()).description(fields.getDescription())
                    .components(getComponentNames(fields)).build();
        }).collect(Collectors.toList());
    }

    private List<String> getComponentNames(IssueFields fields) {
        return fields.getComponents().stream().map(JiraComponent::getName).collect(Collectors.toList());
    }

    private String getParentKey(Issue issue) {
        Issue parent = issue.getFields().getParent();
        if (parent != null) {
            return parent.getKey();
        }
        return null;
    }
}

