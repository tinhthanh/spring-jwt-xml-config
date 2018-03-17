package com.pal.intern.config.api;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;

/**
 *
 * @author tyler.intern
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
    
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> exceptionHandlerForBadRequest(HttpMessageNotReadableException e, HttpServletRequest request) {
        
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setCode(HttpStatus.BAD_REQUEST.value());
        apiError.setUrl(apiError.getFullURL(request));
        apiError.setErrors(Arrays.asList(e.getMessage()));
        return new ResponseEntity<>(apiError, apiError.getStatus());
        
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ApiError> exceptionHandlerForMethodNoTAllowed(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        apiError.setCode(HttpStatus.METHOD_NOT_ALLOWED.value());
        apiError.setUrl(apiError.getFullURL(request));
        apiError.setErrors(Arrays.asList(e.getMessage()));
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    
    @ExceptionHandler(HttpServerErrorException.class)
    @ResponseBody
    public ResponseEntity<ApiError> exceptionHandlerForServerError(HttpServerErrorException e, HttpServletRequest request) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setUrl(apiError.getFullURL(request));
        apiError.setErrors(Arrays.asList(e.getMessage()));
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
