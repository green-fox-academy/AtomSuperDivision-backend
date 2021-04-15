package com.greenfox.fedexasd.controller;

import com.greenfox.fedexasd.repository.MemeRepository;
import com.greenfox.fedexasd.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemeControllerIntegrationTest {

  @Autowired
  private UserService userService;

  @Autowired
  private MemeRepository memeRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void givenGetMemesByTrending_whenUserDoesNotExist_thenThrowsUserDoesNotExistException()
      throws Exception {
    mockMvc.perform(get("/memes/trending"))
        .andExpect(status().isUnauthorized());
  }
}