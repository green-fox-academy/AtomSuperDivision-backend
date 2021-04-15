package com.greenfox.fedexasd.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class LogService {

  public String readFile(String filename) {
    Path filePath = Paths.get(filename);
    StringBuilder text = new StringBuilder();
    try {
      List<String> lines = Files.readAllLines(filePath);
      for (String line : lines) {
        text.append(line).append("\n");
      }
    } catch (IOException e) {
      System.out.println("No such file");
    }
    return text.toString();
  }

  public String numberOfLogins() throws IOException {
    Path filePath = Paths.get("meme.log");
    List<String> lines = Files.readAllLines(filePath);

    int numberOfLogins = 0;
    for (String line : lines) {
      if (line.contains("POST /login")) {
        numberOfLogins++;
      }
    }
    return "The number of logins is: " + numberOfLogins;
  }

  public String numberOfRegistrations() throws IOException {
    Path filePath = Paths.get("meme.log");
    List<String> lines = Files.readAllLines(filePath);

    int numberOfRegistrations = 0;
    for (String line : lines) {
      if (line.contains("POST /register")) {
        numberOfRegistrations++;
      }
    }
    return "The number of new registrations is: " + numberOfRegistrations;
  }

  public String numberOfErrors() throws IOException {
    Path filePath = Paths.get("meme.log");
    List<String> lines = Files.readAllLines(filePath);

    int numberOfErrors = 0;
    for (String line : lines) {
      if (line.contains("ERROR")) {
        numberOfErrors++;
      }
    }
    return "The number of errors is: " + numberOfErrors;
  }

}

