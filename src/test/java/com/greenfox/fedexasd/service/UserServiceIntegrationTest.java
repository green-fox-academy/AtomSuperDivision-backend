package com.greenfox.fedexasd.service;

import com.greenfox.fedexasd.exception.UserDoesNotExistException;
import com.greenfox.fedexasd.exception.UsernameAlreadyTakenException;
import com.greenfox.fedexasd.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserServiceIntegrationTest {
  @Autowired
  UserService userService;

  @Test
  public void givenFindByUsername_whenMockRepoReturnsNotEmptyOptional_thenReturnsValidUser()
      throws UsernameAlreadyTakenException, UserDoesNotExistException {
    User newUser = new User("lofaszka", "pass");
    userService.addUser(newUser);
    assertEquals(newUser.getUsername(), userService.getUserByUsername("lofaszka").getUsername());
  }

  @Test(expected = UserDoesNotExistException.class)
  public void givenFindByUsername_whenMockRepoReturnsNotEmptyOptional_thenThrowsException() throws Exception {
    User newUser = new User("asdasd", "pass");
    userService.addUser(newUser);
    assertEquals(newUser.getUsername(), userService.getUserByUsername("asd").getUsername());
  }
}
