package com.myportifolio.algamoneyapi.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorMessageAgregator {
    private String developerMessage;
    private String userMessage;
}
