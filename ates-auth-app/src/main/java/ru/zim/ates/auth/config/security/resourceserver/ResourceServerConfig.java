package ru.zim.ates.auth.config.security.resourceserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import ru.zim.ates.common.application.config.security.AuthHeaderHandlingFilter;

/**
 * @Author : Amran Hosssain on 6/24/2020
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "ates";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID) //TODO разобраться зачем это
                .stateless(false);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        //-- define URL patterns to enable OAuth2 security
        http.addFilterBefore(new AuthHeaderHandlingFilter(), SecurityContextPersistenceFilter.class).
                anonymous().disable()
                .requestMatchers().antMatchers("/auth/**")
                .and().authorizeRequests()
                .antMatchers("/auth/**").authenticated()
                //.antMatchers("/auth/**").access("hasRole('ADMIN') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_PATIENT')")
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
