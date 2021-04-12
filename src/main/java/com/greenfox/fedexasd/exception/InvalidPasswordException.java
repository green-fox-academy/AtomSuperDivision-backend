package com.greenfox.fedexasd.exception;

public class InvalidPasswordException extends Exception {
  public String getErrorMessage() {
    return "Wrong password!";
  }
}
