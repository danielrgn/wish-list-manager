package com.challenge.luizalabs.commons;

import io.micrometer.core.instrument.util.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;


public abstract class SchemaGenericTest {

  @SneakyThrows
  protected void validateSchema(final String json, final String resourceFileName) {
    @Cleanup final InputStream in = this.getClass().getResourceAsStream(resourceFileName);
    final String schema = IOUtils.toString(in, StandardCharsets.UTF_8);
    SchemaLoader.builder()
        .schemaJson(new JSONObject(new JSONTokener(schema)))
        .draftV7Support()
        .build()
        .load()
        .build()
        .validate(new JSONTokener(json).nextValue());
  }
}
