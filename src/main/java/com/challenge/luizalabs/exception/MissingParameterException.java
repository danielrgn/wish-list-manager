package com.challenge.luizalabs.exception;

public class MissingParameterException extends BaseControllerException {

  private static final long serialVersionUID = -1366479614080419696L;

  public MissingParameterException(Object... parameters) {
    super(parameters);
  }

}
