package org.dxworks.dxplatform.jiraminer.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssueType {
    private String name;
    private String description;
    @JsonProperty("subtask")
    private boolean isSubTask;
}
