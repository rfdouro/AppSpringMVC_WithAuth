/*
 * -----------------
 * -----------------
 * -----------------
 */
package br.org.rfdouro.appspringmvc.config;

import br.org.rfdouro.appspringmvc.config.security.CustomAuthenticationProvider;
import br.org.rfdouro.appspringmvc.handlers.LoginInterceptor;
import br.org.rfdouro.appspringmvc.handlers.LoginInterceptorWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 *
 * @author romulo.douro
 *
 * https://www.boraji.com/spring-mvc-5-static-resources-handling-example
 */
@Configuration
@ComponentScan("br.org.rfdouro.appspringmvc")
@EnableWebMvc
@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

 @Autowired
 private CustomAuthenticationProvider authProvider;

 @Override
 public void configureViewResolvers(ViewResolverRegistry registry) {
  //registry.jsp("/WEB-INF/jsp/", ".jsp");
  InternalResourceViewResolver resolver = new InternalResourceViewResolver();
  resolver.setPrefix("/WEB-INF/jsp/");
  resolver.setSuffix(".jsp");
  resolver.setViewClass(JstlView.class);
  registry.viewResolver(resolver);
 }

 @Override
 public void addResourceHandlers(ResourceHandlerRegistry registry) {
  // Register resource handler
  registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");//.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
 }

 @Override
 public void addInterceptors(InterceptorRegistry registry) {
  registry.addInterceptor(new LoginInterceptor())
          //caso queira em separado 
          //cada modulo deve ser adicionado aqui
          //.addPathPatterns("/pessoa/**")
          //.addPathPatterns("/ws/pessoa/**")
          .excludePathPatterns("/ws/**")
          .excludePathPatterns("/**/login/**");
  registry.addInterceptor(new LoginInterceptorWS())
          .addPathPatterns("/ws/**")
          .excludePathPatterns("/**/login/**");
 }

 /**
  * https://spring.io/blog/2015/06/08/cors-support-in-spring-framework
  *
  * @param registry
  */
 @Override
 public void addCorsMappings(CorsRegistry registry) {
  //registry.addMapping("/webservice/**");
  registry.addMapping("/**");
 }

 @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
 @Override
 public AuthenticationManager authenticationManagerBean() throws Exception {
  return super.authenticationManagerBean();
 }

 @Override
 public void configure(AuthenticationManagerBuilder auth) throws Exception {
  auth.authenticationProvider(authProvider);

  // cria uma conta default
  /*auth.inMemoryAuthentication()
          .withUser("a")
          .password("p")
          .roles("ADMIN");*/
 }

 @Override
 protected void configure(HttpSecurity http) throws Exception {
  http.authorizeRequests().anyRequest().authenticated()
          .and()
          .httpBasic();
 }

 @SuppressWarnings("deprecation")
 @Bean
 public static NoOpPasswordEncoder passwordEncoder() {
  return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
 }

}
