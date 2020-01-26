package org.dxworks.dxplatform.jiraminer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.dxworks.dxplatform.jiraminer.configuration.JiraMinerConfiguration;
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

		JiraMinerConfiguration jiraMinerConfiguration = new JiraMinerConfiguration();

		JiraRestClientFactory jiraRestClientFactory = new JiraRestClientFactory(jiraMinerConfiguration);
		JiraRestClient jiraRestClient = jiraRestClientFactory.createByAuthenticationType();

		JiraMiner jiraMiner = new JiraMiner(jiraRestClient, jiraMinerConfiguration);

		List<JiraIssueDTO> issues = jiraMiner.getIssues();

		ensureResultsFolderExists();
		writeIssuesToFile(jiraMinerConfiguration.getProjectID(), issues);
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

}
