package com.careerbooker.server.annotation.advisor;

import com.careerbooker.server.dto.response.ErrorResponse;
import com.careerbooker.server.mapper.ResponseGenerator;
import com.careerbooker.server.util.ResponseCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
@Log4j2
public class ExceptionHandlerController
{
    @Autowired
    private ResponseGenerator responseGenerator;


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex,
                                                                HttpServletRequest request, Locale locale)
    {
        log.error(ex);
        ErrorResponse errorResponse = responseGenerator.generateExceptionErrorResponse(request, ex);
        errorResponse.setErrorCode(ResponseCode.USER_GET_SUCCESS);
        errorResponse.setErrorDescription(ex.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
