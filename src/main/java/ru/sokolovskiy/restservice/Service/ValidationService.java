package ru.sokolovskiy.restservice.Service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.sokolovskiy.restservice.Exception.ValidationFailedException;

@Service
public interface ValidationService {
    void isValid(BindingResult bindingResult) throws ValidationFailedException;
}
