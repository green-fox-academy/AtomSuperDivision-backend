package com.greenfox.fedexasd.exception;

public class InvalidGenreException extends Exception {
  public String message() {
    return "not valid genre";
  }
}
