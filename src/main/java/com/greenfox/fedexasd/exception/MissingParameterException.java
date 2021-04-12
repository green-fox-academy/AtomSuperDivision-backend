package com.greenfox.fedexasd.exception;

public class MissingParameterException extends Exception {
  private String status = "error";

  public MissingParameterException(String message) {
    super(message);
  }
}
