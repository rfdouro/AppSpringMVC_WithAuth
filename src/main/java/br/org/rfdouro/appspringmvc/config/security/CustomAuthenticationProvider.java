/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.rfdouro.appspringmvc.config.security;

import java.util.ArrayList;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 *
 * @author romulo.douro
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

 @Override
 public Authentication authenticate(Authentication authentication)
         throws AuthenticationException {

  String name = authentication.getName();
  String password = authentication.getCredentials().toString();

  // use the credentials
  // and authenticate against the third-party system
  return new UsernamePasswordAuthenticationToken(
          name, password, new ArrayList());

 }

 @Override
 public boolean supports(Class<?> authentication) {
  return authentication.equals(
          UsernamePasswordAuthenticationToken.class);
 }
}
