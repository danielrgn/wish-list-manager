package com.challenge.luizalabs.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.SneakyThrows;

public abstract class ControllerGenericTest extends ContextGenericTest {

  protected static final String PATH_RESOURCE = "/v1/";

  @SneakyThrows
  protected String getJsonMapped(final Object object) {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    return ow.writeValueAsString(object);
  }

  protected static Properties getProperties() {
    InputStream is = ClassLoader.getSystemResourceAsStream("application.yml");
    Properties config = new Properties();
    try {
      config.load(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return config;
  }
}
