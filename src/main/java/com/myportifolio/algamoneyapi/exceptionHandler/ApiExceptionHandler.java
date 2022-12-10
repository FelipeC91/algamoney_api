package com.myportifolio.algamoneyapi.exceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
@PropertySource("classpath:messages.properties")
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

//    @Value("${mensagem.invalida}")
//    String messageSource;

    @Autowired
    MessageSource messageSource;
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var userMessage = messageSource.getMessage("mensagem.invalida",null, LocaleContextHolder.getLocale());
        var developerMessage = ex.getCause().getMessage();
        var

        return super.handleExceptionInternal(ex, new ErrorMessageAgregator(developerMessage, userMessage), headers, HttpStatus.BAD_REQUEST, request);
    }
}
