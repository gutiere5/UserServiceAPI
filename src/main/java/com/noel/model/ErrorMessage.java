package com.noel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
    //401 Unauthorized
    //404 Not Found
    private Integer code;
    private String message;
}