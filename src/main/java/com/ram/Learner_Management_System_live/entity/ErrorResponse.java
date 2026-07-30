package com.ram.Learner_Management_System_live.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private HttpStatusCode statusCode;
    private String message;
    private Long timeStamp;
    private String stackTrace;

}
