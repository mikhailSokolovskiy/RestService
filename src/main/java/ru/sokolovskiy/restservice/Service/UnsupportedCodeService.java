package ru.sokolovskiy.restservice.Service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.sokolovskiy.restservice.Exception.UnsupportedCodeException;

@Service
public class UnsupportedCodeService implements UnsupportedService {
    @Override
    public void isValid(BindingResult bindingResult) throws UnsupportedCodeException {
        if (bindingResult.hasErrors()) {
            throw new UnsupportedCodeException(bindingResult.getFieldError().toString());
        }

        String code = bindingResult.getFieldValue("uid").toString();
        if ("123".equals(code)) {
            throw new UnsupportedCodeException("Uid не может быть 123");
        }
    }
}
