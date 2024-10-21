package ru.sokolovskiy.restservice.Service;

import org.springframework.stereotype.Service;
import ru.sokolovskiy.restservice.Model.Response;

@Service
public interface ModifyResponseService {

    Response modify(Response response);

}
