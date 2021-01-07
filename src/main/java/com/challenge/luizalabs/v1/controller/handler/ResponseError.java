package com.challenge.luizalabs.v1.controller.handler;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ResponseError implements Serializable {

  private static final long serialVersionUID = -4682076860908920999L;
  private List<ErrorMessage> errorMessages;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  @ToString
  @Builder
  public static class ErrorMessage implements Serializable {

    private static final long serialVersionUID = 1952575470352539304L;

    private String developerMessage;
    private String userMessage;
    private int errorCode;
  }

}
