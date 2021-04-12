package com.greenfox.fedexasd.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Order(value = Integer.MIN_VALUE)
public class CorsConfigurer extends OncePerRequestFilter {

  private String accessControlAllowOrigin;

  public CorsConfigurer(@Value("${cors.access-control-allow-origin}") String accessControlAllowOrigin) {
    this.accessControlAllowOrigin = accessControlAllowOrigin.replaceAll("/$", "");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
    response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    response.addHeader("Access-Control-Max-Age", "36000");
    response.setHeader("Access-Control-Allow-Headers",
        "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, "
            + "Access-Control-Request-Headers, Authorization, X-meme-token");
    response.addHeader("Access-Control-Expose-Headers", "*");
    response.addHeader("Access-Control-Allow-Credentials", "true");

    if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
      response.setStatus(200);
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
