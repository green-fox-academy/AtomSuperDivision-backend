package com.greenfox.fedexasd.controller;

import com.greenfox.fedexasd.model.MemeDTO;
import com.greenfox.fedexasd.security.JwtUtil;
import com.greenfox.fedexasd.service.MemeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemeControllerUnitTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  MemeService mockedMemeService;

  @MockBean
  JwtUtil mockedJwtUtil;

  @Test
  @WithMockUser
  public void givenGetAllMemes_whenRepoIsNotEmpty_thenReturnsAllMemes() throws Exception {
    List<MemeDTO> memes =
        Arrays.asList(
            new MemeDTO(1L, "asd", "https://", 2L, 5L, 145555L, 1L, Timestamp.valueOf("2021-02-22 16:10:55.07"), "Bond"),
            new MemeDTO(2L, "lol", "https://again", 1L, 0L, 12L, 123456L, Timestamp.valueOf("2021-02-22 16:10:55.07"),
                "James"));

    when(mockedMemeService.getAllMemes()).thenReturn(memes);
    mockMvc.perform(get("/meme"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("[0].caption", is(memes.get(0).getCaption())))
        .andExpect(jsonPath("[0].image", is(memes.get(0).getImage())))
        .andExpect(jsonPath("[0].username", is(memes.get(0).getUsername())))
        .andExpect(jsonPath("[1].caption", is(memes.get(1).getCaption())))
        .andExpect(jsonPath("[1].image", is(memes.get(1).getImage())))
        .andExpect(jsonPath("[1].username", is(memes.get(1).getUsername())))
        .andDo(print());
  }
}