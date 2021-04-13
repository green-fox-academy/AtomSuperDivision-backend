package com.greenfox.fedexasd.exception;

public class CommentNotExistException extends Exception{
  @Override
  public String getMessage() {
    return "No comment found with this ID";
  }
}
