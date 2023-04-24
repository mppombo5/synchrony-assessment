package me.mppombo.synchronyapi.exception;

import me.mppombo.synchronyapi.utility.assembler.ApiUserNotFoundModelAssembler;
import me.mppombo.synchronyapi.utility.assembler.ApiUserUsernameTakenModelAssembler;
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
    public ResponseEntity<EntityModel<ErrorBody>> userNotFoundHandler(ApiUserNotFoundException ex, WebRequest req) {
        logger.info("ApiUser w/ ID={} not found, sending 404", ex.getRequestedId());
        ErrorBody notFoundBody = new ErrorBody(
                new Date(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                req.getDescription(false));

        EntityModel<ErrorBody> notFoundModel = notFoundAssembler.toModel(notFoundBody);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundModel);
    }

    @ExceptionHandler(ApiUserUsernameTakenException.class)
    public ResponseEntity<EntityModel<ErrorBody>> usernameTakenHandler(ApiUserUsernameTakenException ex, WebRequest req) {
        logger.info("ApiUser w/ username='{}' exists, sending 409", ex.getUsername());
        ErrorBody usernameTakenBody = new ErrorBody(
                new Date(),
                HttpStatus.CONFLICT.name(),
                ex.getMessage(),
                req.getDescription(false));

        EntityModel<ErrorBody> usernameTakenModel = usernameTakenAssembler.toModel(usernameTakenBody);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(usernameTakenModel);
    }
}
