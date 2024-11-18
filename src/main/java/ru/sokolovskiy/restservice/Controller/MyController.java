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
import ru.sokolovskiy.restservice.Service.*;
import ru.sokolovskiy.restservice.Util.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final UnsupportedCodeService unsupportedCodeService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifyRequestService;
    private final AnnualBonusService annualBonusService;
    private final QuarterlyBonusService quarterlyBonusService;

    @Autowired
    public MyController(ValidationService validationService, UnsupportedCodeService unsupportedCodeService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        ModifyRequestService modifyRequestService,
                        AnnualBonusService annualBonusService,
                        QuarterlyBonusService quarterlyBonusService) {
        this.validationService = validationService;
        this.unsupportedCodeService = unsupportedCodeService;
        this.modifyResponseService = modifyResponseService;
        this.modifyRequestService = modifyRequestService;
        this.annualBonusService = annualBonusService;
        this.quarterlyBonusService = quarterlyBonusService;
    }


    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {
        log.info("request: {}", request);

        if (bindingResult.hasErrors()) {
            return handleValidationErrors(request, bindingResult);
        }

        Response response = createInitialResponse(request);

        try {
            double bonus = calculateBonus(request);
            response.setAnnualBonus(bonus);

            validateRequest(bindingResult);

        } catch (ValidationFailedException | UnsupportedCodeException e) {
            return handleKnownException(response, e);
        } catch (Exception e) {
            return handleUnknownException(response, e);
        }

        modifyResponseService.modify(response);
        modifyRequestService.modify(request);

        log.info("Final response: {}", response);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Response> handleValidationErrors(Request request, BindingResult bindingResult) {
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(error -> String.format("Field: %s, Error: %s", error.getField(), error.getDefaultMessage()))
                .reduce((err1, err2) -> err1 + "; " + err2)
                .orElse("Validation errors occurred");

        log.error("Validation errors: {}", errorMessage);

        Response errorResponse = createErrorResponse(request,
                Codes.FAILED,
                ErrorCodes.VALIDATION_EXCEPTION,
                String.valueOf(ErrorMessages.VALIDATION));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private Response createInitialResponse(Request request) {
        return Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
    }

    private double calculateBonus(Request request) {
        if (request.getPosition().isManager()) {
            log.info("Calculating quarterly bonus for manager");
            return quarterlyBonusService.calculate(
                    request.getPosition(),
                    request.getSalary(),
                    request.getBonus(),
                    request.getWorkDays());
        } else {
            log.info("Calculating annual bonus for non-manager");
            return annualBonusService.calculate(
                    request.getPosition(),
                    request.getSalary(),
                    request.getBonus(),
                    request.getWorkDays());
        }
    }

    private void validateRequest(BindingResult bindingResult) throws ValidationFailedException, UnsupportedCodeException {
        validationService.isValid(bindingResult);
        unsupportedCodeService.isValid(bindingResult);
        log.info("Request validation passed");
    }

    private ResponseEntity<Response> handleKnownException(Response response, Exception e) {
        log.error("Known exception occurred: {}", e.getMessage());
        response.setCode(Codes.FAILED);
        response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
        response.setErrorMessage(ErrorMessages.valueOf(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Response> handleUnknownException(Response response, Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage());
        response.setCode(Codes.FAILED);
        response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
        response.setErrorMessage(ErrorMessages.UNKNOWN);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Response createErrorResponse(Request request, Codes code, ErrorCodes errorCode, String errorMessage) {
        return Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(code)
                .errorCode(errorCode)
                .errorMessage(ErrorMessages.valueOf(errorMessage))
                .build();
    }


}
