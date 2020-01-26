package org.dxworks.dxplatform.jiraminer.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueSearchResult {
    private int startIndex;
    private int maxResults;
    private int total;
    private List<Issue> issues;
}
