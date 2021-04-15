package com.greenfox.fedexasd.service;

import com.greenfox.fedexasd.exception.MissingParameterException;
import com.greenfox.fedexasd.exception.UserDoesNotExistException;
import com.greenfox.fedexasd.exception.UsernameAlreadyTakenException;
import com.greenfox.fedexasd.model.RegisterRequestDTO;
import com.greenfox.fedexasd.model.User;
import com.greenfox.fedexasd.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceUnitTest {
  @Mock
  UserRepository mockedUserRepository;
  UserService userService;
  User user;
  UserDetailsService userDetailsService;

  @Before
  public void setup() {
    mockedUserRepository = Mockito.mock(UserRepository.class);

    userService = new UserService(userDetailsService, mockedUserRepository);
    user = new User("user", "pass");
  }

  @Test
  public void givenGetUserByUsername_whenRepoReturnsNotEmptyOptional_thenReturnsValidUser() throws Exception {
    String expectedUsername = "user";
    when(mockedUserRepository.findUserByUsername("user")).thenReturn(Optional.of(user));
    User actualUser = userService.getUserByUsername("user");
    assertEquals(expectedUsername, actualUser.getUsername());
  }

  @Test(expected = UserDoesNotExistException.class)
  public void givenGetUserByUsername_whenRepoReturnsEmptyOptional_thenThrowsException() throws Exception {
    when(mockedUserRepository.findUserByUsername("user")).thenReturn(Optional.of(user));
    User actualUser = userService.getUserByUsername("asd");
  }


  @Test(expected = MissingParameterException.class)
  public void givenRegister_whenUsernameIsNull_thenThrowsMissingFieldException()
      throws MissingParameterException, UsernameAlreadyTakenException {
    userService.register(new RegisterRequestDTO(null, "pass"));
  }

  @Test(expected = MissingParameterException.class)
  public void givenRegister_whenPasswordIsNull_thenThrowsMissingFieldException()
      throws MissingParameterException, UsernameAlreadyTakenException {
    userService.register(new RegisterRequestDTO("user", null));
  }

  @Test(expected = MissingParameterException.class)
  public void givenRegister_whenRequestIsNull_thenThrowsMissingFieldException()
      throws MissingParameterException, UsernameAlreadyTakenException {
    userService.register(new RegisterRequestDTO(null, null));
  }


  @Test(expected = MissingParameterException.class)
  public void givenRegister_whenUsernameIsEmpty_thenThrowsMissingFieldException()
      throws MissingParameterException, UsernameAlreadyTakenException {
    userService.register(new RegisterRequestDTO("", "password"));
  }

  @Test(expected = MissingParameterException.class)
  public void givenRegister_whenPasswordIsEmpty_thenThrowsMissingFieldException()
      throws MissingParameterException, UsernameAlreadyTakenException {
    userService.register(new RegisterRequestDTO("user", ""));
  }
}

