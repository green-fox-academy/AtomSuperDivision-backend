package com.greenfox.fedexasd.security;

import com.greenfox.fedexasd.model.User;
import com.greenfox.fedexasd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  private UserService userService;

  @Autowired
  public CustomOAuth2UserService(UserService userservice) {
    this.userService = userservice;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauth2User = super.loadUser(userRequest);
    Map<String, Object> attributes = oauth2User.getAttributes();

    String username = (String) attributes.get("login");
    Optional<User> optionalUser = this.userService.findOptionalByUsername(username);

    if (!optionalUser.isPresent()) {
      User user = new User();
      user.setUsername(username);
      this.userService.saveUser(user);
    }
    return oauth2User;
  }
}
