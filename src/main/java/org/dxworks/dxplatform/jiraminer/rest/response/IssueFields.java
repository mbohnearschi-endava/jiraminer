package org.dxworks.dxplatform.jiraminer.rest.response;

import lombok.Data;

import java.util.List;

@Data
public class IssueFields {
    private Issue parent;
    private IssueType issuetype;
    private IssueStatus status;
    private List<JiraComponent> components;
    private String summary;
    private String description;
    private String created;
    private String updated;
}
