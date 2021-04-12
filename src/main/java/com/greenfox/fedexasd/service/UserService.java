package com.greenfox.fedexasd.service;

import com.greenfox.fedexasd.exception.InvalidPasswordException;
import com.greenfox.fedexasd.exception.MissingParameterException;
import com.greenfox.fedexasd.exception.UserDoesNotExistException;
import com.greenfox.fedexasd.exception.UsernameAlreadyTakenException;
import com.greenfox.fedexasd.model.LoginRequestDTO;
import com.greenfox.fedexasd.model.RegisterRequestDTO;
import com.greenfox.fedexasd.model.User;
import com.greenfox.fedexasd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
  private final UserDetailsService userDetailsService;
  private UserRepository userRepository;

  @Autowired
  public UserService(UserDetailsService userDetailsService, UserRepository userRepository) {
    this.userDetailsService = userDetailsService;
    this.userRepository = userRepository;
  }

  public User getUserByUsername(String username) throws UserDoesNotExistException {
    return userRepository.findUserByUsername(username).orElseThrow(UserDoesNotExistException::new);
  }

  public void addUser(User user) throws UsernameAlreadyTakenException {
    Optional<User> optionalUser = userRepository.findUserByUsername(user.getUsername());
    if (optionalUser.isPresent()) {
      throw new UsernameAlreadyTakenException();
    }
    userRepository.save(user);
  }

  public User register(RegisterRequestDTO registerRequestDTO)
      throws MissingParameterException, UsernameAlreadyTakenException {
    if (registerRequestDTO == null) {
      throw new MissingParameterException("Missing parameter(s): password, username, kingdomName!");
    }
    registerValidate(registerRequestDTO.getUsername(), registerRequestDTO.getPassword());
    Optional<User> findById = userRepository.findById(registerRequestDTO.getUsername());
    if (findById.isPresent()) {
      throw new UsernameAlreadyTakenException();
    }
    return setup(registerRequestDTO);
  }

  private User setup(RegisterRequestDTO registerRequestDTO) {
    User user = new User(registerRequestDTO.getUsername(), registerRequestDTO.getPassword());
    saveUser(user);
    return userRepository.save(user);
  }

  private void registerValidate(String username, String password) throws MissingParameterException {
    List<String> missingFields = new ArrayList<>();
    if (username == null || username.equals("")) {
      missingFields.add("username");
    }
    if (password == null || password.equals("")) {
      missingFields.add("password");
    }
    if (!missingFields.isEmpty()) {
      String joined = String.join(", ", missingFields);
      throw new MissingParameterException("Missing parameter(s): " + joined + "!");
    }
  }

  public void saveUser(User user) {
    userRepository.save(user);
  }

  public Map<String, String> loginValidate(String username, String password) {
    Map<String, String> missingFields = new HashMap<>();
    missingFields.put("username", username);
    missingFields.put("password", password);
    return missingFields;
  }

  public void setupValidate(Map<String, String> fields) throws MissingParameterException {
    List<String> missingFields = new ArrayList<>();
    for (Map.Entry<String, String> entry : fields.entrySet()) {
      if (entry.getValue() == null || entry.getValue().equals("")) {
        missingFields.add(entry.getKey());
      }
    }
    if (!missingFields.isEmpty()) {
      String joined = String.join(", ", missingFields);
      throw new MissingParameterException("Missing parameter(s): " + joined + "!");
    }
  }

  public void readLoginRequest(LoginRequestDTO loginRequest)
      throws MissingParameterException {
    if (loginRequest == null) {
      throw new MissingParameterException("Missing parameter(s): password, username!");
    }

    Map<String, String> loginDetails =
        loginValidate(loginRequest.getUsername(), loginRequest.getPassword());
    setupValidate(loginDetails);
  }

  private void checkPassword(LoginRequestDTO loginRequest, UserDetails userDetails) throws InvalidPasswordException {
    if (!loginRequest.getPassword().equals(userDetails.getPassword())) {
      throw new InvalidPasswordException();
    }
  }

  public UserDetails authenticateUser(LoginRequestDTO loginRequest)
      throws UsernameNotFoundException, InvalidPasswordException,
      MissingParameterException {
    readLoginRequest(loginRequest);
    UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
    checkPassword(loginRequest, userDetails);
    return userDetails;
  }

  public Optional<User> findOptionalByUsername(String username) {
    return userRepository.findUserByUsername(username);
  }
}
