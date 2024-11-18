package ru.sokolovskiy.restservice.Model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    //уникальный идентификатор сообщения
    @NotBlank(message = "UID не может быть пустым")
    @Size(max = 32, message = "UID не должно быть больше 32 символов")
    private String uid;

    //уникальный идентификатор операции
    @NotBlank(message = "operationUid не может быть пустым")
    @Size(max = 32, message = "operationUid не должно быть больше 32 символов")
    private String operationUid;

    //имя системы отправителя
    private String systemName;
    //время создания сообщения
    private String systemTime;
    //наименование ресурса
    private String source;

    //должность сотрудника
    private Positions position;
    //зарплата
    private double salary;
    //бонус
    private double bonus;
    //кол-во рабочих дней
    private int workDays;

    //Уникальный идентификатор коммуникации
    @Min(value = 1, message = "communicationId не может быть меньше 1")
    @Max(value = 100000, message = "communicationId не может быть больше 100000")
    private int communicationId;
    //Уникальный идентификатор шаблона
    private int templateId;
    //код продукта
    private int productCode;
    //смс код
    private int smsCode;

    //время отправки для проверки времени между сервисами
    private LocalDateTime requestTime;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                ", requestTime=" + requestTime +
                '}';
    }

}
