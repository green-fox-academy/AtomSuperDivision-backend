package com.greenfox.fedexasd.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestResponseLoggingFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    log.info("Logging Request: " + req.getMethod() + " " + req.getRequestURI());

    long start = System.currentTimeMillis();
    chain.doFilter(request, response);
    long end = System.currentTimeMillis();
    long execution = end - start;

    log.info("Logging Response: " + res.getStatus() + ", execution time: " +  execution);
  }
}

