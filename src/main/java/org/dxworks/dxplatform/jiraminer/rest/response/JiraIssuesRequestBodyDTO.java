package org.dxworks.dxplatform.jiraminer.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JiraIssuesRequestBodyDTO {
    private String jql;
    private int startAt;
    private int maxResults;
    private List<String> fields;
}
