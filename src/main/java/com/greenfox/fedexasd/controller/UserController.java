package com.greenfox.fedexasd.controller;

import com.greenfox.fedexasd.exception.InvalidPasswordException;
import com.greenfox.fedexasd.exception.MissingParameterException;
import com.greenfox.fedexasd.exception.UsernameAlreadyTakenException;
import com.greenfox.fedexasd.model.LoginRequestDTO;
import com.greenfox.fedexasd.model.LoginSuccessResponseDTO;
import com.greenfox.fedexasd.model.RegisterRequestDTO;
import com.greenfox.fedexasd.model.RegisterSuccessResponseDTO;
import com.greenfox.fedexasd.model.User;
import com.greenfox.fedexasd.security.JwtUtil;
import com.greenfox.fedexasd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

  private UserService userService;
  private JwtUtil jwtUtil;

  @Autowired
  public UserController(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/register")
  @ResponseBody
  public ResponseEntity<Object> registration(@RequestBody(required = false) RegisterRequestDTO registerRequestDTO)
      throws UsernameAlreadyTakenException, MissingParameterException {
    User saved = userService.register(registerRequestDTO);
    return ResponseEntity.ok(new RegisterSuccessResponseDTO(saved.getUsername()));
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<Object> login(@RequestBody(required = false) LoginRequestDTO loginRequest)
      throws MissingParameterException, InvalidPasswordException {
    UserDetails userDetails = userService.authenticateUser(loginRequest);
    String token = jwtUtil.generateToken(userDetails.getUsername());
    return ResponseEntity.ok(new LoginSuccessResponseDTO("ok", token));
  }
}
