package com.challenge.luizalabs.exception;

public class EntityNotFoundException extends BaseControllerException {

  private static final long serialVersionUID = -1366479614080419696L;

  public EntityNotFoundException(Object... parameters) {
    super(parameters);
  }

}
