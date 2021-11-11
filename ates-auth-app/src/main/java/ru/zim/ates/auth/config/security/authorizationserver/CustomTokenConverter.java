package ru.zim.ates.auth.config.security.authorizationserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;
import ru.zim.ates.auth.mapper.AppUserMapper;
import ru.zim.ates.auth.repository.AppUserRepository;

/**
 * @Author : Amran Hosssain on 6/23/2020
 */
@Slf4j
@Aspect
@Component
public class CustomTokenConverter extends JwtAccessTokenConverter {

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppUserMapper appUserMapper;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        OAuth2AccessToken oAuth2AccessToken;
        appUserRepository.findByUsername(authentication.getName()).ifPresent(appUser -> {
            ObjectMapper objectMapper = new ObjectMapper();
            additionalInfo.putAll(objectMapper.convertValue(appUserMapper.toResponceDto(appUser), Map.class));
        });
        oAuth2AccessToken = super.enhance(accessToken, authentication);
        oAuth2AccessToken.getAdditionalInformation().putAll(additionalInfo);
        return oAuth2AccessToken;
    }
}
