package com.challenge.luizalabs.exception;

public class EntityAlreadyExistsException extends BaseControllerException {

  private static final long serialVersionUID = -1366479614080419696L;

  public EntityAlreadyExistsException(Object... parameters) {
    super(parameters);
  }

}
