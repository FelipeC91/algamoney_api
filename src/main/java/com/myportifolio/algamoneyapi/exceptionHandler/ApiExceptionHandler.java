package com.myportifolio.algamoneyapi.exceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
@PropertySource("classpath:messages.properties")
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

//    @Value("${mensagem.invalida}")
//    String messageSource;

    @Autowired
    MessageSource messageSource;

    List<ErrorMessageAgregator> errorMessageAgregatorList = new ArrayList<>();

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var userMessage = messageSource.getMessage("mensagem.invalida",null, LocaleContextHolder.getLocale());
        var developerMessage = ex.getCause().getMessage();

        this.errorMessageAgregatorList.add(new ErrorMessageAgregator(developerMessage, userMessage));

        return super.handleExceptionInternal(ex, errorMessageAgregatorList, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        makeMultipleErrorMessageAgregators(ex.getBindingResult());
        return super.handleExceptionInternal(ex, this.errorMessageAgregatorList,headers, HttpStatus.BAD_REQUEST, request);
    }


    private void  makeMultipleErrorMessageAgregators(BindingResult bindingResult){
        bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    var devMsg = fieldError.toString();
                    var usrMsg = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                    return new ErrorMessageAgregator(devMsg,usrMsg);
                })
                .forEach(this.errorMessageAgregatorList::add);
    }

//    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAcessException(EmptyResultDataAccessException ex, WebRequest request) {
        var devMsg = ex.toString();
        var usrMsg = messageSource.getMessage("recurso-nao-encontrado", null, LocaleContextHolder.getLocale());
        var errorMssgAgregator = new ErrorMessageAgregator(devMsg,usrMsg);
        return handleExceptionInternal(ex,errorMssgAgregator,new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }
}
