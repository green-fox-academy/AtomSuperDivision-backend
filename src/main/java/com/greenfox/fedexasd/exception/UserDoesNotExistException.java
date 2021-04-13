package com.greenfox.fedexasd.exception;

public class UserDoesNotExistException extends Exception{
  public String message(){return "username does not exist";}
}
