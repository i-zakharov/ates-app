package ru.zim.ates.tasktracker.config.security.resourceserver;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;

/**
 * @Author : Amran Hosssain on 6/27/2020
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${client_id:admin}")
    private String clientId;

    @Value("${client_credential:qwerty}")
    private String clientSecret;

    @Autowired
    private AuthenticationHost authenticationHost;

    @Bean
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(clientSecret);
        tokenServices.setCheckTokenEndpointUrl(authenticationHost.getCheckTokenEndpointUrl());
        return tokenServices;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
        authenticationManager.setTokenServices(tokenServices());
        return authenticationManager;
    }

    @Component
    public static class AuthenticationHost {
        private URI hostUri;

        public AuthenticationHost(@Value("${authorization_host:http://localhost:8081}") String hostUri) {
            this.hostUri = URI.create(hostUri);
        }

        public String getCheckTokenEndpointUrl() {
            return hostUri.resolve("/oauth/check_token").toString();
        }

        public String getLoginUrl() {
            return hostUri.resolve("/login").toString();
        }
    }
}
