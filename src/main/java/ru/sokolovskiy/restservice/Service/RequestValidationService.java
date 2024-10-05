package ru.sokolovskiy.restservice.Service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.sokolovskiy.restservice.Exception.ValidationFailedException;

import java.util.Objects;

@Service
public class RequestValidationService implements ValidationService {

    @Override
    public void isValid(BindingResult bindingResult) throws ValidationFailedException {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult.getFieldError().toString());
        }
    }
}
