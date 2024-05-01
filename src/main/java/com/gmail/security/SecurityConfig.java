package com.gmail.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(clientRegistration());
    }

    private ClientRegistration clientRegistration() {
        return ClientRegistration.withRegistrationId("google")  //keycloak  - change same in application.properties if want keycloak authentication
//                .clientId("gmail_client")
//                .clientSecret("LODaWPF7p8JmjwwHoZjBXKLH1zHm3LEH")
                .clientId("161912283701-ikji4r9grp7p83an6gh74h1uig89ck6j.apps.googleusercontent.com")
                .clientSecret("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")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/google")   //check here for https
                .scope("openid", "profile", "email")
                //For google authentication
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                //For keycloak authentication
//                .authorizationUri("http://localhost:8081/auth/realms/gmailkey/protocol/openid-connect/auth")
//                .tokenUri("http://localhost:8081/auth/realms/gmailkey/protocol/openid-connect/token")
//                .userInfoUri("http://localhost:8081/auth/realms/gmailkey/protocol/openid-connect/userinfo")
//                .userNameAttributeName(IdTokenClaimNames.SUB)
                .clientName("Google")   //Keycloak
                .build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            authorities.forEach(authority -> {
                if (OAuth2UserAuthority.class.isInstance(authority)) {
                    OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
                    mappedAuthorities.addAll(mapScopesToAuthorities(oauth2UserAuthority.getAttributes()));
                }
            });
            return mappedAuthorities;
        };
    }

    private Set<GrantedAuthority> mapScopesToAuthorities(Map<String, Object> attributes) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String scope : attributes.keySet()) {
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope.toUpperCase()));
        }
        return authorities;
    }
}
