package ru.zim.ates.auth.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import ru.zim.ates.auth.model.AppUser;
import ru.zim.ates.auth.repository.AppUserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService, ClientDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsernameAndIsActiveIsTrue(username)
                .map(appUser -> User.builder()
                        .username(appUser.getUsername())
                        .password(appUser.getPassword())
                        .roles(appUser.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user(%s) found in db.", username)));
    }


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return appUserRepository.findByUsername(clientId)
                .map(this::toClientDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No client(%s) found in db.", clientId)));
    }

    private BaseClientDetails toClientDetails(AppUser appUser) {
        BaseClientDetails out = new BaseClientDetails(appUser.getUsername(),
                null,
                "read",
                "client_credentials",
                appUser.getRole().name());
        out.setClientSecret(appUser.getPassword());
        return out;
    }
}
