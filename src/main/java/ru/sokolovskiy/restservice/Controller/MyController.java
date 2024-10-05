package ru.sokolovskiy.restservice.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sokolovskiy.restservice.Exception.ValidationFailedException;
import ru.sokolovskiy.restservice.Model.Request;
import ru.sokolovskiy.restservice.Model.Response;
import ru.sokolovskiy.restservice.Service.ValidationService;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class MyController {

    private final ValidationService validationService;

    @Autowired
    public MyController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(simpleDateFormat.format(new Date()))
                .code("Success")
                .errorCode("")
                .errorMessage("")
                .build();


        try {
            validationService.isValid(bindingResult);
            if (request.getUid().equals("123")) {
                response.setCode("failed");
                response.setErrorCode("ValidationException");
                response.setErrorMessage("Uid не может быть 123");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (ValidationFailedException e) {
            response.setCode("failed");
            response.setErrorCode("ValidationException");
            response.setErrorMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setCode("failed");
            response.setErrorCode("UnknownException");
            response.setErrorMessage("Произошла непредвиденная ошибка");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
