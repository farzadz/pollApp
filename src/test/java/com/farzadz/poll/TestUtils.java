package com.farzadz.poll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static String asJsonString(Object val) throws JsonProcessingException {
    return mapper.writeValueAsString(val);
  }
}
