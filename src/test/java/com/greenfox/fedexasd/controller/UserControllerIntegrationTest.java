package com.greenfox.fedexasd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.fedexasd.model.RegisterRequestDTO;
import com.greenfox.fedexasd.model.User;
import com.greenfox.fedexasd.repository.UserRepository;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {"JWT_SECRET_KEY"})
public class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  ObjectMapper objectMapper = new ObjectMapper();
  private RegisterRequestDTO registerRequestDto;
  User mockUser = new User();


  @Test
  public void givenRegisterUrl_whenMockMvc_thenStatusOk_andReturnsNewUser() throws Exception {
    registerRequestDto = new RegisterRequestDTO("lol", "pass");
    String request = objectMapper.writeValueAsString(registerRequestDto);

    mockMvc.perform(post("/register")
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", Is.is("lol")))
        .andExpect(status().isOk());
  }

  @Test
  public void givenRegisterUrl_whenMockMvcMissingUsername_thenStatusBadRequest_andThrowException()
      throws Exception {
    registerRequestDto = new RegisterRequestDTO("", "pass");
    String request = objectMapper.writeValueAsString(registerRequestDto);

    mockMvc.perform(post("/register").content(request).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("status", is("error")))
        .andExpect(jsonPath("message", is("Missing parameter(s): username!")))
        .andDo(print());
  }

  @WithMockUser
  @Test
  public void givenMemeURL_whenMockMVC_thenStatusOK_andReturnsMemeList() throws Exception {

    mockMvc.perform(get("/meme"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(6)))
        .andDo(print());
  }

  @Test
  public void givenLogin_whenAuthnIsValid_thenReturnsOk() throws Exception {
    mockUser.setUsername("user");
    mockUser.setPassword("pass");
    userRepository.save(mockUser);
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(mockUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("status", is("ok")))
        .andExpect(jsonPath("token", matchesRegex("^[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*$")))
        .andDo(print());
  }

  @Test
  public void givenLogin_whenUsernameIsNotFound_thenReturnsUnauthorized() throws Exception {
    mockUser.setUsername("!user");
    mockUser.setPassword("pass");
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(mockUser)))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  public void givenLogin_whenPasswordIsInvalid_thenReturnsUnauthorized()
      throws Exception {
    User user = new User("user", "pass");
    mockUser.setUsername("user");
    mockUser.setPassword("!pass");
    userRepository.save(user);
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(mockUser)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("status", is("error")))
        .andExpect(jsonPath("message", is("Wrong password!")))
        .andDo(print());
  }

  @Test
  public void givenLogin_whenRequestBodyIsInvalid_thenReturnsBadRequest() throws Exception {
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(mockUser)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("status", is("error")))
        .andExpect(jsonPath("message", is("Missing parameter(s): password, username!")))
        .andDo(print());
  }

  @Test
  public void givenLogin_whenRequestBodyIsEmpty_thenReturnsBadRequest() throws Exception {
    mockMvc.perform(post("/login"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("status", is("error")))
        .andExpect(jsonPath("message", is("Missing parameter(s): password, username!")))
        .andDo(print());
  }
}
