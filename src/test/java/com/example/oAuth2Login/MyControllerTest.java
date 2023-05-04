package com.example.oAuth2Login;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)

public class MyControllerTest {

    @InjectMocks
    @Spy
    MyController myController;

    @Test
    @DisplayName("mock parent class")
    public void test() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("nameAttributeKey", "Ashish");
        GrantedAuthority authorities = new SimpleGrantedAuthority("attributes");
        OAuth2User principal = new DefaultOAuth2User(Collections.singletonList(authorities), attributes,
                "nameAttributeKey");
        String authorizedClientRegistrationId = "authorizedClientRegistrationId";
        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(principal, Collections.singletonList(authorities),
                authorizedClientRegistrationId);
        doNothing().when((BaseController) myController).logging(any(OAuth2AuthenticationToken.class));
        myController.logging(authentication);
    }

}
