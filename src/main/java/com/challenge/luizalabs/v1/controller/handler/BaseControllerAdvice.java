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
        .errorMessage(
            "Was encountered an error when processing your request."
                + " We apologize for the inconvenience.")
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
                .errorMessage(
                    format("Field {0} is required and can not be empty", field.getField()))
                .build());

      } else if ("Email".equalsIgnoreCase(field.getCode())) {

        errorMessages.add(
            ResponseError.ErrorMessage.builder()
                .errorMessage(
                    format(
                        "Invalid field {0} - it must be filled with a valid email",
                        field.getField()))
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
        .errorMessage("Method not allowed")
        .build());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final EntityNotFoundException ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .errorMessage(
            format("You attempted to get a {0}, but did not find any", ex.getParameters()))
        .build());
  }

  @ExceptionHandler(EntityAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final EntityAlreadyExistsException ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .errorMessage(
            format("You attempted to create {0} already exists", ex.getParameters()))
        .build());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final MethodArgumentTypeMismatchException ex) {
    if (ex.getRequiredType() == UUID.class) {
      return Collections.singletonList(ResponseError.ErrorMessage.builder()
          .errorMessage(MessageFormat.format(
              "Invalid field {0} - it must be filled with a valid UUID", ex.getName()))
          .build());
    } else {
      return Collections.singletonList(ResponseError.ErrorMessage.builder()
          .errorMessage(MessageFormat.format(
              "Invalid field {0} - it is not allowed", ex.getName()))
          .build());
    }
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final EmptyResultDataAccessException ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .errorMessage(
            "We not found this entity with the id requested."
                + " We apologize for the inconvenience.")
        .build());
  }

  @ExceptionHandler(InternalServerErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final InternalServerErrorException ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .errorMessage(
            "Was encountered an error when processing your request."
                + " We apologize for the inconvenience.")
        .build());
  }
}
