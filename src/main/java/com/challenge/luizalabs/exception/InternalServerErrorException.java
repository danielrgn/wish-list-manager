package com.challenge.luizalabs.exception;

public class InternalServerErrorException extends BaseControllerException {

  private static final long serialVersionUID = -1366479614080419696L;

  public InternalServerErrorException(Object... parameters) {
    super(parameters);
  }

}
