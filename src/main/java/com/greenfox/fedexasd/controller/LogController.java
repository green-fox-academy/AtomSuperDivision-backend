package com.greenfox.fedexasd.controller;

import com.greenfox.fedexasd.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LogController {
  private LogService logService;

  @Autowired
  public LogController(LogService logService) {
    this.logService = logService;
  }

  @GetMapping("/logfile")
  public ResponseEntity<String> getLogFile() {
    String logFile = logService.readFile("meme.log");
    return ResponseEntity.ok(logFile);
  }

  @GetMapping("/login")
  public ResponseEntity<String> numberOfLogins() throws IOException {
    return ResponseEntity.ok(logService.numberOfLogins());
  }

  @GetMapping("/register")
  public ResponseEntity<String> numberOfRegistrations() throws IOException {
    return ResponseEntity.ok(logService.numberOfRegistrations());
  }

  @GetMapping("/error")
  public ResponseEntity<String> numberOfErrors() throws IOException {
    return ResponseEntity.ok(logService.numberOfErrors());
  }
}
