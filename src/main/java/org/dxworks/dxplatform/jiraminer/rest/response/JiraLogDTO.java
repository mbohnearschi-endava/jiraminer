package org.dxworks.dxplatform.jiraminer.rest.response;

import lombok.Data;

import java.util.List;

@Data
class JiraLogDTO {
    private List<JiraIssueDTO> issues;
}
