package org.dxworks.dxplatform.jiraminer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfiguration;
import org.dxworks.dxplatform.jiraminer.rest.client.JiraMinerUnauthorizedException;
import org.dxworks.dxplatform.jiraminer.rest.client.JiraRestClient;
import org.dxworks.dxplatform.jiraminer.rest.client.JiraRestClientFactory;
import org.dxworks.dxplatform.jiraminer.rest.response.JiraIssueDTO;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
//		oldMain(args);

        log.info("Starting Jira Miner...");

        JiraMinerConfiguration jiraMinerConfiguration = new JiraMinerConfiguration();

        JiraRestClientFactory jiraRestClientFactory = new JiraRestClientFactory(jiraMinerConfiguration);
        JiraRestClient jiraRestClient = jiraRestClientFactory.createByAuthenticationType();

        JiraMiner jiraMiner = new JiraMiner(jiraRestClient, jiraMinerConfiguration);

        List<JiraIssueDTO> issues = null;
        try {
            issues = jiraMiner.getIssues();
        } catch (JiraMinerUnauthorizedException e) {
            log.error("Error: Unauthorized. Please revise your authentication method and try again. \n" +
                    "If you are using cookie based authentication, please renew the cookie!");
            System.exit(1);
        }

        log.info("Writing results to file...");
        ensureResultsFolderExists();
        writeIssuesToFile(jiraMinerConfiguration.getProjectID(), issues);
        log.info("Finished Jira Miner.");
    }

    private static void ensureResultsFolderExists() {
        File directory = new File("results");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static void writeIssuesToFile(String projectID, List<JiraIssueDTO> issues) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("results/" + projectID + "-issues.json"), issues);
        } catch (IOException e) {
            log.error("Could not write result file!", e);
            throw e;
        }
    }

//	private static void oldMain(String[] args) throws Exception {
//		if (args.length == 0) {
//			throw new IllegalArgumentException("No command specified. Use one of " + Command.names());
//		}
//
//		PropertiesClient propertiesClient = new PropertiesClient();
//		JiraOAuthClient jiraOAuthClient = new JiraOAuthClient(propertiesClient);
//
//		List<String> argumentsWithoutFirst = Arrays.asList(args).subList(1, args.length);
//
//		new OAuthClient(propertiesClient, jiraOAuthClient).execute(Command.fromString(args[0]), argumentsWithoutFirst);
//	}
}
