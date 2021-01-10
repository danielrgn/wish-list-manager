package com.challenge.luizalabs.service.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import com.challenge.luizalabs.commons.ControllerGenericTest;
import com.challenge.luizalabs.payload.ChallengeApiPayloadFileEnum;

import java.io.File;
import java.nio.file.Files;

import lombok.SneakyThrows;

public class ChallengeApiWiremock {

  public static void setUpChallengeApiSuccess() {
    stubFor(get(anyUrl())
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(getPayload(ChallengeApiPayloadFileEnum.SEND_OK.getFileName()))));
  }

  public static void setUpChallengeApiNotFound() {
    stubFor(get(anyUrl())
        .willReturn(aResponse()
            .withStatus(404)
            .withHeader("Content-Type", "application/json")
            .withBody(getPayload(ChallengeApiPayloadFileEnum.SEND_NOT_FOUND.getFileName()))));
  }

  public static void setUpChallengeApiInternalServerError() {
    stubFor(get(anyUrl())
        .willReturn(aResponse()
            .withStatus(500)
            .withHeader("Content-Type", "application/json")));
  }

  @SneakyThrows
  private static String getPayload(final String fileName) {
    final File file = new File(ControllerGenericTest.class.getClassLoader().getResource("payloads/" + fileName)
        .getFile());

    return new String(Files.readAllBytes(file.toPath()));
  }
}
