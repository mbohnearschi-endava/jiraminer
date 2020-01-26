package org.dxworks.dxplatform.jiraminer.rest.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class JiraIssueDTO {
    private String key;
    private String issueType;
    private String parentKey;
    private String status;
    private String startDate;
    private String endDate;
    private String summary;
    private String description;
    private List<String> components;
}
