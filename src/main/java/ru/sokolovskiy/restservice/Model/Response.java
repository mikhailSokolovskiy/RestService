package ru.sokolovskiy.restservice.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    //уникальный идентификатор сообщения
    private String uid;
    //уникальный идентификатор операции
    private String operationUid;
    //время создания сообщения
    private String systemTime;
    //код запроса (error/success)
    private Codes code;
    //премия годовая или квартальная в зависимости от должности
    private double annualBonus;
    //код ошибки
    private ErrorCodes errorCode;
    //сооьщение ошибки
    private ErrorMessages errorMessage;

}
