package br.org.rfdouro.appspringmvc.controllers.login;

import br.org.rfdouro.appspringmvc.config.security.TokenAuthenticationService;
import br.org.rfdouro.appspringmvc.controllers.DefaultController;
import br.org.rfdouro.appspringmvc.models.AccountCredentials;
import br.org.rfdouro.appspringmvc.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author romulo.douro
 */
@Controller
@RequestMapping("/login")
public class LoginController extends DefaultController {

 @RequestMapping(value = {"", "/"})
 public ModelAndView login(ModelMap map) {
  map.addAttribute("tituloInternoPagina", "Comissione [Login]");
  map.addAttribute("MODULO", "<li class=\"active\">Comissione</li><li class=\"Login\">Módulos</li>");

  return new ModelAndView("login/login", map);
 }

 @RequestMapping(value = {"/loga"})
 public String loga(Model map, String login, String senha,
         @RequestParam(required = false) String redir,
         HttpServletResponse response,
         HttpServletRequest request) throws IOException {
  String pagina = "forward:/login";
  map.addAttribute("redir", redir);

  this.setRepositorio();
  if (login != null && senha != null && !senha.equals("")) {

   this.session.setAttribute("USULOGADO", login);
   //login ok armazena o cookie
   {
    Cookie cookie = new Cookie("login", Util.encode(login));
    //cookie.setPath("/");
    cookie.setPath(request.getContextPath());//importantíssimo
    cookie.setMaxAge(60 * 60 * 24 * 10);//240 horas = 10 dias
    response.addCookie(cookie);
    cookie = new Cookie("senha", Util.encode(senha));
    //cookie.setPath("/");
    cookie.setPath(request.getContextPath());//importantíssimo
    cookie.setMaxAge(60 * 60 * 24 * 10);//240 horas = 10 dias
    response.addCookie(cookie);
   }

  }
  pagina = "redirect:/home";
  if (redir != null && !redir.equals("")) {
   pagina = "redirect:" + redir;
  }
  return pagina;
 }

 @RequestMapping(value = {"/logaWS"})
 @ResponseBody
 public String logaWS(Model map, String login, String senha,
         HttpServletResponse response,
         HttpServletRequest request) throws IOException {
  String msg = "Login efetuado com sucesso";

  try {
   this.setRepositorio();
   if (login != null && senha != null && !senha.equals("")) {

    this.session.setAttribute("USULOGADO", login);
    //faz o processo de autenticação por token
    {
     System.out.println("->" + login);
     System.out.println("->" + senha);

     AccountCredentials credentials = new AccountCredentials();
     credentials.setUsername(login);
     credentials.setPassword(senha);

     UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
             credentials.getUsername(),
             credentials.getPassword(),
             Collections.EMPTY_LIST
     );
     Authentication auth = authenticationManager.authenticate(authReq);
     /*SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication(auth);*/
     TokenAuthenticationService.addAuthentication(response, auth.getName());

    }

   }
  } catch (Exception ex) {
   msg = "ERRO " + Util.getMsgErro(ex);
  }

  return msg;
 }

 @RequestMapping(value = {"/confirmaDesloga"})
 public String confirmaDesloga(Model map, HttpServletResponse response) {
  String pagina = "redirect:/";

  //request.getSession().setAttribute("USULOGADO", null);
  this.session.setAttribute("USULOGADO", null);

  {
   Cookie cookie = new Cookie("login", null);
   //cookie.setPath("/");
   cookie.setPath(request.getContextPath());//importantíssimo
   cookie.setMaxAge(0);//0
   response.addCookie(cookie);
   cookie = new Cookie("senha", null);
   //cookie.setPath("/");
   cookie.setPath(request.getContextPath());//importantíssimo
   cookie.setMaxAge(0);//0
   response.addCookie(cookie);
  }

  return pagina;
 }

}
