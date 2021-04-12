package com.greenfox.fedexasd.security;

import com.greenfox.fedexasd.repository.UserRepository;
import com.greenfox.fedexasd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private UserService userService;
  private JwtUtil jwtUtil;
  private String authorizedRedirectUri;

  @Autowired
  public OAuth2LoginSuccessHandler(UserService userService,
                                   UserRepository userRepository,
                                   JwtUtil jwtUtil, @Value("${redirect.uri}") String authorizedRedirectUri) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
    this.authorizedRedirectUri = authorizedRedirectUri;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
    DefaultOAuth2User userPrincipal = (DefaultOAuth2User) authentication.getPrincipal();
    String username = userPrincipal.getAttribute("login");

    if (response.isCommitted()) {
      logger.debug("Response has already been committed. Unable to redirect to ");
      return;
    }

    String targetUrl = determineTargetUrl(username);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  private String determineTargetUrl(String username) {
    String accessToken = jwtUtil.generateToken(username);

    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", accessToken);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.setAll(tokens);

    return UriComponentsBuilder.fromUriString(authorizedRedirectUri)
        .queryParams(params)
        .build().toUriString();
  }
}
