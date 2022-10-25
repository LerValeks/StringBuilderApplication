package miniStringBuilderApp.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class ApiError {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    // for unexpected Error
    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, List<ApiSubError> subErrors, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
        this.subErrors = subErrors;

    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();

    }

    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }


    //Field validation Errors
    private void addValidationFieldErrorToSubErorrs(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }


    //Field Validation Errors Constrains
    private void parseValidationError(ConstraintViolation<?> cv) {
        this.addValidationFieldErrorToSubErorrs(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    //Method invoked in
    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::parseValidationError);
    }


    private void parseValidationFieldError(FieldError fieldError) {
        this.addValidationFieldErrorToSubErorrs(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::parseValidationFieldError);
    }

    private void addValidationObjectErrorToSubErorrs(String object, String message) {
        addSubError(new ApiValidationError(object, message));
    }

    private void parseValidationObjectError(ObjectError objectError) {
        this.addValidationObjectErrorToSubErorrs(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::parseValidationObjectError);
    }


    //Invalid Format Error
    private void addInvalidFormatErrorToSubErrors(String field, String message) {
        addSubError(new ApiValidationError("StringInput", field, message));
    }

    private void parseInvalidFormatError(Map<String, String> error) {

        Map.Entry<String, String> entry = error.entrySet().iterator().next();
        String message = entry.getValue();


        this.addInvalidFormatErrorToSubErrors(
                entry.getKey(),
                message
        );
    }

    public void addInvalidFormatError(List<Map<String, String>> errors) {
        errors.forEach(this::parseInvalidFormatError);
    }

    public void addInvalidFormatErrorToList(InvalidFormatException iex) {
        List<Map<String, String>> errors = new ArrayList<>();
        iex.getPath().forEach(reference -> {
            Map<String, String> error = new HashMap<>();
            error.put(reference.getFieldName(), iex.getOriginalMessage());
            errors.add(error);

        });
        addInvalidFormatError(errors);
    }


}
