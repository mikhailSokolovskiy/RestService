package ru.sokolovskiy.restservice.Service;

import org.springframework.validation.BindingResult;
import ru.sokolovskiy.restservice.Exception.ValidationFailedException;

public interface UnsupportedService {
    void isValid(BindingResult bindingResult) throws ValidationFailedException;

}
