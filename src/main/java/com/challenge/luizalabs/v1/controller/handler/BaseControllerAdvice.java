package com.challenge.luizalabs.v1.controller.handler;

import static java.text.MessageFormat.format;

import com.challenge.luizalabs.exception.EntityAlreadyExistsException;
import com.challenge.luizalabs.exception.EntityNotFoundException;
import com.challenge.luizalabs.exception.InternalServerErrorException;
import com.challenge.luizalabs.exception.MissingParameterException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


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

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(
      final NoHandlerFoundException ex) {

    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .developerMessage("Resource not found")
        .userMessage("Resource not found")
        .errorCode(20022).build());
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
            format("You attempted to create {0}, but already exists", ex.getParameters()))
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

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public List<ResponseError.ErrorMessage> exception(final ConstraintViolationException ex) {
    final Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

    final List<ResponseError.ErrorMessage> errorMessages = new ArrayList<>();

    constraintViolations.forEach(constraint -> {
      final String messageTemplate = constraint.getConstraintDescriptor().getMessageTemplate();
      String field = "";
      for (Path.Node node : constraint.getPropertyPath()) {
        field = node.getName();
      }
      if ("{javax.validation.constraints.Max.message}".equals(messageTemplate)) {
        final String size = constraint
            .getConstraintDescriptor().getAttributes().get("value").toString();
        errorMessages.add(ResponseError.ErrorMessage.builder()
            .developerMessage(
                format("Invalid query parameter {0} - "
                    + "it must be filled with a value lesser or equals than {1}", field, size))
            .userMessage(
                format("Invalid field {0} - "
                    + "it must be filled with a value lesser or equals than {1}", field, size))
            .errorCode(20088)
            .build());
      } else if ("{javax.validation.constraints.Min.message}".equals(messageTemplate)) {
        final String size = constraint
            .getConstraintDescriptor().getAttributes().get("value").toString();
        errorMessages.add(ResponseError.ErrorMessage.builder()
            .developerMessage(
                format("Invalid query parameter {0} - "
                    + "it must be filled with a value greater or equals than {1}", field, size))
            .userMessage(
                format("Invalid field {0} - "
                    + "it must be filled with a value greater or equals than {1}", field, size))
            .errorCode(20087)
            .build());
      } else if ("{javax.validation.constraints.Size.message}".equals(messageTemplate)) {
        final Integer minSize = Integer.valueOf(
            constraint.getConstraintDescriptor().getAttributes().get("min").toString()
        );
        final Integer maxSize = Integer.valueOf(
            constraint.getConstraintDescriptor().getAttributes().get("max").toString()
        );
        final String value = String.valueOf(constraint.getInvalidValue());
        if (value.length() > maxSize) {
          errorMessages.add(ResponseError.ErrorMessage.builder()
              .developerMessage(
                  format("Invalid query parameter {0} - "
                      + "it must be filled with a value lesser or equals than {1}", field, maxSize))
              .userMessage(
                  format("Invalid field {0} - "
                      + "it must be filled with a value lesser or equals than {1}", field, maxSize))
              .errorCode(20088)
              .build());
        } else if (value.length() < minSize) {
          errorMessages.add(ResponseError.ErrorMessage.builder()
              .developerMessage(
                  format("Invalid query parameter {0} - it "
                      + "must be filled with a value greater or equals than {1}", field, minSize))
              .userMessage(
                  format("Invalid field {0} - it "
                      + "must be filled with a value greater or equals than {1}", field, minSize))
              .errorCode(20087)
              .build());
        }
      } else if ("{javax.validation.constraints.Email.message}".equals(messageTemplate)) {
        errorMessages.add(ResponseError.ErrorMessage.builder()
            .developerMessage(
                format("Invalid query parameter {0} - it "
                    + "must be filled with a valid email", field))
            .userMessage(
                format("Invalid field {0} - it "
                    + "must be filled with a valid email", field))
            .errorCode(20013)
            .build());
      }
    });
    return errorMessages;
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

  @ResponseBody
  @ExceptionHandler(MissingParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ResponseError.ErrorMessage> exceptionHandler(final MissingParameterException ex) {
    return Collections.singletonList(ResponseError.ErrorMessage.builder()
        .developerMessage(
            format("Missing {0} parameter {1}", ex.getParameters()))
        .userMessage(
            format("Field {1} is required and can not be empty", ex.getParameters()))
        .errorCode(20001)
        .build());
  }

}
