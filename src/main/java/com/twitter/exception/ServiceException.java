package com.twitter.exception;


import com.twitter.dto.ErrorDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ServiceException  extends Exception {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private List<ErrorDTO> nestedErrors;
    private ErrorMessageCode errorMessageCode;

    public ServiceException(Throwable exception) {
        super(exception);
    }

    public ServiceException(String exception) {
        super(exception);
    }

    public ServiceException(HttpStatus status,Throwable exception) {
        super(exception);
        this.status=status;
    }

    public ServiceException(HttpStatus status, String exceptionMessage, Throwable exception) {
        super(exceptionMessage, exception);
        this.status=status;
    }

    public ServiceException(HttpStatus status, String exceptionMessage) {
        super(exceptionMessage);
        this.status=status;
    }

    public ServiceException( String exceptionMessage, ErrorMessageCode errorMessageCode) {
        super(exceptionMessage);
        this.errorMessageCode = errorMessageCode;
    }

    public ServiceException(HttpStatus status, String exceptionMessage, ErrorMessageCode errorMessageCode) {
        super(exceptionMessage);
        this.status = status;
        this.errorMessageCode = errorMessageCode;
    }

    public ServiceException(HttpStatus status, String exceptionMessage, ErrorMessageCode errorMessageCode, List<ErrorDTO> nestedErrors) {
        super(exceptionMessage);
        this.status = status;
        this.errorMessageCode = errorMessageCode;
        this.nestedErrors = nestedErrors;
    }

}