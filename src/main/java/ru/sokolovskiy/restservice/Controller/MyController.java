package ru.sokolovskiy.restservice.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sokolovskiy.restservice.Exception.UnsupportedCodeException;
import ru.sokolovskiy.restservice.Exception.ValidationFailedException;
import ru.sokolovskiy.restservice.Model.*;
import ru.sokolovskiy.restservice.Service.ModifyResponseService;
import ru.sokolovskiy.restservice.Service.UnsupportedCodeService;
import ru.sokolovskiy.restservice.Service.ValidationService;
import ru.sokolovskiy.restservice.Util.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final UnsupportedCodeService unsupportedCodeService;

    private final ModifyResponseService modifyResponseService;

    @Autowired
    public MyController(ValidationService validationService, UnsupportedCodeService unsupportedCodeService,
                        @Qualifier("ModifySystemTimeResponseService")ModifyResponseService modifyResponseService) {
        this.validationService = validationService;
        this.unsupportedCodeService = unsupportedCodeService;
        this.modifyResponseService = modifyResponseService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {

        log.info("request: {}", request);

        // Проверка на наличие ошибок в bindingResult
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMsg.append("Field: ").append(error.getField())
                        .append(", Error: ").append(error.getDefaultMessage()).append("; ");
            });
            log.error("Validation errors: {}", errorMsg.toString());

            Response errorResponse = Response.builder()
                    .uid(request.getUid())
                    .operationUid(request.getOperationUid())
                    .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                    .code(Codes.FAILED)
                    .errorCode(ErrorCodes.VALIDATION_EXCEPTION)
                    .errorMessage(ErrorMessages.VALIDATION)
                    .build();

            log.info("Response updated after validation errors: {}", errorResponse);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("Initial response created: {}", response);

        try {
            validationService.isValid(bindingResult);
            log.info("Request validation passed for: {}", request);

            unsupportedCodeService.isValid(bindingResult);
            log.info("Unsupported code validation passed for: {}", request);

        } catch (ValidationFailedException e) {
            log.error("Validation failed: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Response updated after validation failure: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Unsupported code exception: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Response updated after unsupported code exception: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Response updated after unexpected error: {}", response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyResponseService.modify(response);
        log.info("Response modified by ModifyResponseService: {}", response);

        return new ResponseEntity<>(modifyResponseService.modify(response), HttpStatus.OK);
    }
}
