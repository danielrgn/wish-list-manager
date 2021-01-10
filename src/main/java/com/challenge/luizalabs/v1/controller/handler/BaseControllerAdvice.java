package com.challenge.luizalabs.v1.controller.handler;

import static java.text.MessageFormat.format;

import com.challenge.luizalabs.exception.EntityAlreadyExistsException;
import com.challenge.luizalabs.exception.EntityNotFoundException;
import com.challenge.luizalabs.exception.InternalServerErrorException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
@Order(1)
public class BaseControllerAdvice {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final Exception ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .developerMessage(
            "Internal server error")
        .userMessage(
            "Was encountered an error when processing your request."
                + " We apologize for the inconvenience.")
        .errorCode(10000)
        .build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final MethodArgumentNotValidException ex) {

    final List<FieldError> fields = ex.getBindingResult().getFieldErrors();

    final List<ResponseError.ErrorMessage> errorMessages = new ArrayList<>();

    fields.forEach(field -> {

      if ("NotNull".equalsIgnoreCase(field.getCode())
          || "NotBlank".equalsIgnoreCase(field.getCode())) {

        errorMessages.add(
            ResponseError.ErrorMessage.builder()
                .developerMessage(
                    format("Missing body parameter {0}", field.getField()))
                .userMessage(
                    format("Field {0} is required and can not be empty", field.getField()))
                .errorCode(20001)
                .build());

      } else if ("Email".equalsIgnoreCase(field.getCode())) {

        errorMessages.add(
            ResponseError.ErrorMessage.builder()
                .developerMessage(
                    format(
                        "Invalid body parameter {0} - it must be filled with a valid email",
                        field.getField()))
                .userMessage(
                    format(
                        "Invalid field {0} - it must be filled with a valid email",
                        field.getField()))
                .errorCode(20013)
                .build());
      } else {

        errorMessages.add(
            ResponseError.ErrorMessage.builder()
                .developerMessage("Malformed request body")
                .userMessage("Malformed request body")
                .errorCode(20020)
                .build());
      }
    });

    return errorMessages;
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(
      final HttpRequestMethodNotSupportedException ex) {

    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .developerMessage("Method not allowed")
        .userMessage("Method not allowed")
        .errorCode(20021).build());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final EntityNotFoundException ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .developerMessage(
            format("{0} not found", ex.getParameters()))
        .userMessage(
            format("You attempted to get a {0}, but did not find any", ex.getParameters()))
        .errorCode(20023)
        .build());
  }

  @ExceptionHandler(EntityAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final EntityAlreadyExistsException ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .developerMessage(
            format("{0} already exists", ex.getParameters()))
        .userMessage(
            format("You attempted to create {0} already exists", ex.getParameters()))
        .errorCode(20033)
        .build());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final MethodArgumentTypeMismatchException ex) {
    if (ex.getRequiredType() == UUID.class) {
      return Collections.singletonList(ResponseError.ErrorMessage.builder()
          .developerMessage(
              MessageFormat.format(
                  "Invalid parameter {0} - it must be filled with a valid UUID",
                  ex.getName()))
          .userMessage(MessageFormat.format(
              "Invalid field {0} - it must be filled with a valid UUID", ex.getName()))
          .errorCode(20012)
          .build());
    } else {
      return Collections.singletonList(ResponseError.ErrorMessage.builder()
          .developerMessage(
              MessageFormat.format("Invalid query parameter {0} - it is not allowed",
                  ex.getName() + "=" + ex.getValue()))
          .userMessage(MessageFormat.format(
              "Invalid field {0} - it is not allowed", ex.getName()))
          .errorCode(20035)
          .build());
    }
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final EmptyResultDataAccessException ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .developerMessage(
            format("{0}", ex.getMessage()))
        .userMessage(
            "We not found this entity with the id requested."
                + " We apologize for the inconvenience.")
        .errorCode(55500)
        .build());
  }

  @ExceptionHandler(InternalServerErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final InternalServerErrorException ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .developerMessage(
            format("Internal server error {0}", ex.getParameters()))
        .userMessage(
            "Was encountered an error when processing your request."
                + " We apologize for the inconvenience.")
        .errorCode(10000)
        .build());
  }
}
