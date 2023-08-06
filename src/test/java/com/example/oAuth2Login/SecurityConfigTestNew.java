package com.example.oAuth2Login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@RunWith(MockitoJUnitRunner.class)
public class SecurityConfigTestNew {

    @InjectMocks
    SecurityConfig securityConfig;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    AuthenticationManagerBuilder authenticationManagerBuilder;


    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ObjectPostProcessor objectPostProcessor;

    @Test
    public void filterChain() throws Exception {
        //securityConfig.filterChain(new HttpSecurity(objectPostProcessor, authenticationManagerBuilder, Collections.emptyMap()));

        HttpSecurity http = Mockito.mock(HttpSecurity.class);
        securityConfig.filterChain(http);
    }
}
