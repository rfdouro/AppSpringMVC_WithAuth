/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.rfdouro.appspringmvc.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 *
 * @author romulo.douro
 */
public class TokenAuthenticationService {

 // EXPIRATION_TIME = 10 dias
 static final long EXPIRATION_TIME = 860_000_000;
 static final String SECRET = "MySecret";
 static final String TOKEN_PREFIX = "Bearer";
 static final String HEADER_STRING = "Authorization";

 public static void addAuthentication(HttpServletResponse response, String username) {
  String JWT = Jwts.builder()
          .setSubject(username)
          .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
          .signWith(SignatureAlgorithm.HS512, SECRET)
          .compact();

  response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
 }

 public static Authentication getAuthentication(HttpServletRequest request) {
  String token = request.getHeader(HEADER_STRING);
  
  //System.out.println(""+request.getParameter("username"));

  if (token != null) {
   // faz parse do token
   String user = Jwts.parser()
           .setSigningKey(SECRET)
           .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
           .getBody()
           .getSubject();

   if (user != null) {
    return new UsernamePasswordAuthenticationToken(user, null, Collections.EMPTY_LIST);
   }
  }
  return null;
 }
}
