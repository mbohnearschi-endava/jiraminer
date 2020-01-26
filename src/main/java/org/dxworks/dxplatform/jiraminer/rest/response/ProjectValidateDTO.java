package org.dxworks.dxplatform.jiraminer.rest.response;

import lombok.Data;

import java.util.List;

@Data
public class ProjectValidateDTO {
    private List<String> errorMessages;

    private ProjectValidateErrorsDTO errors;
}
