package com.challenge.luizalabs.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Value("${oauth.clientSecret}")
  private String clientSecret;

  @Value("${oauth.clientId}")
  private String clientId;

  @Value("${oauth.expiresIn}")
  private int expiresIn;

  @Value("${oauth.scopes}")
  private String scopes;

  @Value("${oauth.grantTypes}")
  private String grantTypes;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) {
    security.checkTokenAccess("isAuthenticated");
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient(clientId)
        .authorizedGrantTypes(grantTypes)
        .secret(passwordEncoder.encode(clientSecret))
        .scopes(scopes)
        .accessTokenValiditySeconds(expiresIn);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints.authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService);
  }
}
