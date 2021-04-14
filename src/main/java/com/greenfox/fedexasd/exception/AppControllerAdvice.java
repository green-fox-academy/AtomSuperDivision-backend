package com.greenfox.fedexasd.exception;

import com.greenfox.fedexasd.model.Comment;
import com.greenfox.fedexasd.model.CommentErrorResponseDTO;
import com.greenfox.fedexasd.model.LoginErrorResponseDTO;
import com.greenfox.fedexasd.model.MemeErrorResponseDTO;
import com.greenfox.fedexasd.model.RegisterErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class AppControllerAdvice {

  private static final Logger log = LoggerFactory.getLogger(AppControllerAdvice.class);

  @ExceptionHandler(UsernameAlreadyTakenException.class)
  ResponseEntity<Object> usernameAlreadyTakenExceptionHandler(UsernameAlreadyTakenException e) {
    return errorResponse(new ResponseEntity<>(new RegisterErrorResponseDTO(e.getMessage()), HttpStatus.CONFLICT), e);
  }

  @ExceptionHandler(MissingParameterException.class)
  ResponseEntity<Object> missingParameterExceptionHandler(MissingParameterException e) {
    return errorResponse(new ResponseEntity<>(new RegisterErrorResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST), e);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  ResponseEntity<Object> invalidPasswordExceptionHandler(InvalidPasswordException e) {
    return errorResponse(
        new ResponseEntity<>(new LoginErrorResponseDTO("error", e.getErrorMessage()), HttpStatus.UNAUTHORIZED), e);
  }

  @ExceptionHandler(UserDoesNotExistException.class)
  ResponseEntity<Object> userDoesNotExistExceptionHandler(UserDoesNotExistException e) {
    return errorResponse(
        new ResponseEntity<>(new LoginErrorResponseDTO("error", e.getMessage()), HttpStatus.UNAUTHORIZED), e);
  }

  @ExceptionHandler(MemeDoesNotExistException.class)
  ResponseEntity<Object> memeDoesNotExistExceptionHandler(MemeDoesNotExistException e) {
    return errorResponse(
        new ResponseEntity<>(new MemeErrorResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST), e);
  }

  @ExceptionHandler(CommentNotExistException.class)
  ResponseEntity<Object> commentNotExistExceptionHandler(CommentNotExistException e) {
    return errorResponse(
        new ResponseEntity<>(new CommentErrorResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST), e);
  }

  protected ResponseEntity<Object> errorResponse(ResponseEntity<Object> responseEntity, Throwable throwable) {
    if (null != throwable) {
      log.error("error caught: " + throwable.getMessage(), throwable);
    } else {
      log.error("unknown error caught in RESTController, {}", responseEntity.getStatusCode());
    }
    return responseEntity;
  }

  @ExceptionHandler(MemeDoesNotExistException.class)
  ResponseEntity<Object> invalidGenreExceptionHandler(InvalidGenreException e) {
    return errorResponse(
        new ResponseEntity<>((e.getMessage()), HttpStatus.BAD_REQUEST), e);
  }
}
