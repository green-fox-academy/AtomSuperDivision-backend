package com.greenfox.fedexasd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.fedexasd.exception.MissingParameterException;
import com.greenfox.fedexasd.model.LoginErrorResponseDTO;
import com.greenfox.fedexasd.model.User;
import com.greenfox.fedexasd.security.AppUserDetails;
import com.greenfox.fedexasd.security.JwtUtil;
import com.greenfox.fedexasd.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerUnitTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UserService mockedUserService;

  @MockBean
  JwtUtil mockedJwtUtil;

  User mockUser = new User();
  ModelMapper modelMapper = new ModelMapper();
  ObjectMapper objectMapper = new ObjectMapper();
  LoginErrorResponseDTO errorResponse = new LoginErrorResponseDTO("error", null);

  @Test
  public void givenLogin_whenRequestBodyIsValid_thenReturnsOk() throws Exception {
    mockUser.setUsername("user");
    mockUser.setPassword("pass");
    AppUserDetails appUserDetails = new AppUserDetails(mockUser);
    UserDetails userDetails = modelMapper.map(appUserDetails, UserDetails.class);
    when(mockedUserService.authenticateUser(any())).thenReturn(userDetails);
    when(mockedJwtUtil.generateToken(any())).thenReturn("^[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*$");
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(mockUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("status", is("ok")))
        .andExpect(jsonPath("token", is("^[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*$")))
        .andDo(print());
  }

  @Test
  public void givenLogin_whenFieldsAreNull_thenReturnsBadRequest()
      throws Exception {
    errorResponse.setMessage("Missing parameter(s): password, username!");
    when(mockedUserService.authenticateUser(any()))
        .thenThrow(new MissingParameterException("Missing parameter(s): password, username!"));
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(mockUser)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("status", is(errorResponse.getStatus())))
        .andExpect(jsonPath("message", is(errorResponse.getMessage())))
        .andDo(print());
  }

  @Test
  public void givenLogin_whenUsernameIsNull_thenReturnsBadRequest()
      throws Exception {
    mockUser.setPassword("pass");
    errorResponse.setMessage("Missing parameter(s): username!");
    when(mockedUserService.authenticateUser(any()))
        .thenThrow(new MissingParameterException("Missing parameter(s): username!"));
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(mockUser)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("status", is(errorResponse.getStatus())))
        .andExpect(jsonPath("message", is(errorResponse.getMessage())))
        .andDo(print());
  }

  @Test
  public void givenLogin_whenPasswordIsNull_thenReturnsBadRequest() throws Exception {
    mockUser.setUsername("user");
    errorResponse.setMessage("Missing parameter(s): password!");
    when(mockedUserService.authenticateUser(any()))
        .thenThrow(new MissingParameterException("Missing parameter(s): password!"));
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(mockUser)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("status", is(errorResponse.getStatus())))
        .andExpect(jsonPath("message", is(errorResponse.getMessage())))
        .andDo(print());
  }
}