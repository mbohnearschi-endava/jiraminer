package org.dxworks.dxplatform.jiraminer.rest.response;

import lombok.Data;

@Data
public class Issue {
    private String key;
    private IssueFields fields;
}
