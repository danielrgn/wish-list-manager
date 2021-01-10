package com.challenge.luizalabs.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChallengeApiPayloadFileEnum {
  SEND_OK("challenge-api-product-ok.json"),
  SEND_NOT_FOUND("challenge-api-product-not-found.json");

  private String fileName;
}
