package ru.sokolovskiy.restservice.Service;


import org.springframework.stereotype.Service;
import ru.sokolovskiy.restservice.Model.Request;

@Service
public interface ModifyRequestService {

    void modify(Request request);

}
