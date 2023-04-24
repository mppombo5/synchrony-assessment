package me.mppombo.synchronyapi.exception;

import me.mppombo.synchronyapi.assembler.ApiUserNotFoundModelAssembler;
import me.mppombo.synchronyapi.assembler.ApiUserUsernameTakenModelAssembler;
import me.mppombo.synchronyapi.exception.apiuser.ApiUserNotFoundException;
import me.mppombo.synchronyapi.exception.apiuser.ApiUserUsernameTakenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ApiUserControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ApiUserControllerExceptionHandler.class);

    private final ApiUserNotFoundModelAssembler notFoundAssembler;
    private final ApiUserUsernameTakenModelAssembler usernameTakenAssembler;

    public ApiUserControllerExceptionHandler(
            ApiUserNotFoundModelAssembler notFoundAssembler,
            ApiUserUsernameTakenModelAssembler usernameTakenAssembler) {
        this.notFoundAssembler = notFoundAssembler;
        this.usernameTakenAssembler = usernameTakenAssembler;
    }


    @ExceptionHandler(ApiUserNotFoundException.class)
    public ResponseEntity<EntityModel<ApiUserErrorBody>> userNotFoundHandler(ApiUserNotFoundException ex, WebRequest req) {
        logger.info("ApiUser w/ ID={} not found, sending 404", ex.getRequestedId());
        ApiUserErrorBody notFoundBody = new ApiUserErrorBody(
                new Date(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                req.getDescription(false));

        EntityModel<ApiUserErrorBody> notFoundModel = notFoundAssembler.toModel(notFoundBody);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundModel);
    }

    @ExceptionHandler(ApiUserUsernameTakenException.class)
    public ResponseEntity<EntityModel<ApiUserErrorBody>> usernameTakenHandler(ApiUserUsernameTakenException ex, WebRequest req) {
        logger.info("ApiUser w/ username='{}' exists, sending 409", ex.getUsername());
        ApiUserErrorBody usernameTakenBody = new ApiUserErrorBody(
                new Date(),
                HttpStatus.CONFLICT.name(),
                ex.getMessage(),
                req.getDescription(false));

        EntityModel<ApiUserErrorBody> usernameTakenModel = usernameTakenAssembler.toModel(usernameTakenBody);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(usernameTakenModel);
    }
}
