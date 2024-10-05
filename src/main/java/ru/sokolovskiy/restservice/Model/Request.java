package ru.sokolovskiy.restservice.Model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @NotBlank(message = "UID не может быть пустым")
    @Size(max = 32, message = "UID не должно быть больше 32 символов")
    private String uid;

    @NotBlank(message = "operationUid не может быть пустым")
    @Size(max = 32, message = "UID не должно быть больше 32 символов")
    private String operationUid;

    private String systemName;
    private String systemTime;
    private String source;

    @Min(value = 1, message = "communicationId не может быть меньше 1")
    @Max(value = 100000, message = "communicationId не может быть больше 100000")
    private int communicationId;

    private int templateId;
    private int productCode;
    private int smsCode;

}
