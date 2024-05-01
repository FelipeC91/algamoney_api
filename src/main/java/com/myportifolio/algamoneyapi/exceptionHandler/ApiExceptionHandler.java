package com.myportifolio.algamoneyapi.exceptionHandler;

import com.myportifolio.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@PropertySource("classpath:messages.properties")
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

//    @Value("${mensagem.invalida}")
//    String messageByPropertie;

    @Autowired
    MessageSource messageSource;

    List<ErrorMessageAgregatorRecord> errorMessageAgregatorList = new ArrayList<>();

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errorMsgAgregator = buildErrorMessagesAgregator("mensagem.invalida", ex, false);
        this.errorMessageAgregatorList.add(errorMsgAgregator);

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
                    return new ErrorMessageAgregatorRecord(devMsg,usrMsg);
                })
                .forEach(this.errorMessageAgregatorList::add);
    }

//    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAcessException(EmptyResultDataAccessException ex, WebRequest request) {
        var errorMsgAgregator = buildErrorMessagesAgregator("recurso.nao-encontrado", ex, false);

        return handleExceptionInternal(ex,errorMsgAgregator,new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,WebRequest request) {
        var errorMsgAgregator = buildErrorMessagesAgregator("recurso.operacao-nao-permitida", ex,true);

        return handleExceptionInternal(ex, errorMsgAgregator,new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    //Service Exception
    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        var errorMsgAgregator = buildErrorMessagesAgregator("Acesso Negado", ex, false);

        return handleExceptionInternal(ex, errorMsgAgregator,new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
     }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex, WebRequest request) {
        var errorMsgAgregator = buildErrorMessagesAgregator("recurso.pessoa.inexistente-ou-inativa", ex, false);

        return handleExceptionInternal(ex, errorMsgAgregator,new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private ErrorMessageAgregatorRecord buildErrorMessagesAgregator(String messageSourceProperty, Exception ex, boolean getRootCause) {
        var devMsg = getRootCause? ExceptionUtils.getRootCauseMessage(ex) : ex.toString();
        var usrMsg = messageSource.getMessage(messageSourceProperty,null, LocaleContextHolder.getLocale());

        return new ErrorMessageAgregatorRecord(devMsg, usrMsg);
    }

}
