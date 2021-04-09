package com.twitter.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonTypeName("ErrorDTO")
@ApiModel(value = "ErrorDTO", description = "DTO to define a Error")
public class ErrorDTO {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5767906253263397432L;

    private List<ErrorDTO> errorDTOs;

    /**
     * Time and date the error was thrown
     */
    @ApiModelProperty(value = "Time and date the error was thrown", position = 1)
    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now();

    /** The code. */
    @ApiModelProperty(value = "Error code", position = 2)
    private String code;

    /** The message. */
    @ApiModelProperty(value = "Error message", position = 3)
    private String message;

    /** The description. */
    @ApiModelProperty(value = "Error description", position = 4)
    private String description;

    /**
     * The name of the service that threw this error
     */
    @ApiModelProperty(value = "The name of the service that threw this error", position = 5)
    private String serviceName;

    /**
     * The error is caused by a bad request from client, it's not due to an internal service.
     * Can be used to tell Hystrix to ignore the bad requests errors.
     */
    @ApiModelProperty(value = "The error is caused by a bad request from client, it's not due to an internal service", position = 6)
    @Builder.Default
    private Boolean isBadRequest = false;

    /**
     * The trace ID
     */
    @ApiModelProperty(value = "The trace ID", position = 7)
    private String traceId;


    @ApiModelProperty(value = "URI to documentation of error codes", position = 8)
    private String uriDoc;

    /**
     * The nested error that threw this error
     */
    @ApiModelProperty(value = "The nested error that threw this error", position = 9)
    private ErrorDTO nestedErrors;

    private String stackTrace;
}
