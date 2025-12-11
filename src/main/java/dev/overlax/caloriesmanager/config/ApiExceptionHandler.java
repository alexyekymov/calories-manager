package dev.overlax.caloriesmanager.config;

import dev.overlax.caloriesmanager.util.exception.AppException;
import dev.overlax.caloriesmanager.util.exception.ErrorType;
import dev.overlax.caloriesmanager.web.payload.ApiError;
import dev.overlax.caloriesmanager.web.payload.ApiSubError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static dev.overlax.caloriesmanager.util.exception.ErrorType.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    static final Map<Class<? extends Throwable>, ErrorType> HTTP_STATUS_MAP = new LinkedHashMap<>() {
        {
            put(NoResourceFoundException.class, NOT_FOUND);
            put(AuthenticationException.class, UNAUTHORIZED);
            put(FileNotFoundException.class, NOT_FOUND);
            put(NoHandlerFoundException.class, NOT_FOUND);
            put(EntityNotFoundException.class, DATA_CONFLICT);
            put(DataIntegrityViolationException.class, DATA_CONFLICT);
            put(IllegalArgumentException.class, BAD_DATA);
            put(ValidationException.class, BAD_REQUEST);
            put(HttpRequestMethodNotSupportedException.class, BAD_REQUEST);
            put(ServletRequestBindingException.class, BAD_REQUEST);
            put(RequestRejectedException.class, BAD_REQUEST);
            put(AccessDeniedException.class, FORBIDDEN);
            put(HttpMessageNotReadableException.class, BAD_REQUEST);
        }
    };

    // TODO: Refactor this method !!!
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> bindExceptionHandler(BindException ex, HttpServletRequest req) {
        List<ApiSubError> bindErrors = collectBindErrors(ex.getBindingResult());

        ApiError error = new ApiError(
                BAD_REQUEST.getStatus(),
                BAD_REQUEST.title,
                "At least one argument isn't valid",
                Instant.now(), // UTC is better
                req.getRequestURI(),
                bindErrors);

        return ResponseEntity.status(BAD_REQUEST.status).body(error);
    }

    private List<ApiSubError> collectBindErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(err -> new ApiSubError(
                        err.getObjectName(),
                        err.getField(),
                        err.getRejectedValue() != null ? err.getRejectedValue().toString() : "",
                        err.getDefaultMessage()))
                .toList();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> dbErrorHandler(
            Exception ex,
            HttpServletRequest req) {

        ErrorType errorType = findErrorType(ex);
        String msg = extractSQLErrorCauseMessage(ex);
        String path = req.getRequestURI();

        ApiError error = createApiError(errorType, msg, path, Collections.emptyList());

        return ResponseEntity.status(errorType.status).body(error);
    }

    // TODO: Refactor this method !!!
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> globalHandler(
            Exception ex,
            HttpServletRequest req) {

        /// asd
        System.out.println(ex.getClass().getSimpleName());
        System.out.println("_____________________");
//        Arrays.stream(ex.getStackTrace()).forEach(System.out::println);
        /// asd

        ErrorType errorType = findErrorType(ex);
        String msg = ex.getLocalizedMessage();
        String path = req.getRequestURI();

        ApiError error = createApiError(errorType, msg, path, Collections.emptyList());

        return ResponseEntity.status(errorType.status).body(error);
    }

    private ErrorType findErrorType(Throwable ex) {
        if (ex instanceof AppException aex) {
            return aex.getErrorType();
        }

        Class<? extends Throwable> exClass = ex.getClass();

        return HTTP_STATUS_MAP.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAssignableFrom(exClass))
                .findAny().map(Map.Entry::getValue).get(); // TODO refactor
    }

    private ApiError createApiError(ErrorType errType, String msg, String path, List<ApiSubError> subErrors) {

        return new ApiError(errType.getStatus(),
                errType.title,
                msg,
                Instant.now(), // UTC is better
                path,
                subErrors);
    }

    private String extractSQLErrorCauseMessage(Throwable t) {
        String raw = t.getMessage();
        return raw.split("ERROR: ")[1].split("\n")[0];
    }
}
