package com.farzadz.poll.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

  private ObjectMapper mapper;

  @Autowired
  public ApiExceptionHandler(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public JsonNode handleIllegalArgumentException(Exception exception) {
    return handle400Error(exception);
  }

  private JsonNode handle400Error(Exception exception) {
    final ObjectNode response = mapper.createObjectNode();
    response.put("status", "400");
    response.put("message", exception.getMessage());
    return response;
  }

}