package com.greenfox.fedexasd.exception;

public class MemeDoesNotExistException extends Exception {
  public String message() {
    return "No meme found with given ID";
  }
}
