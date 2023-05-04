package com.example.oAuth2Login;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.servlet.ModelAndView;

public interface BaseController {
    ModelAndView userDetails(OAuth2AuthenticationToken authentication);

    //some dummy method to junit testing parent mocking in child test case
    default void logging(OAuth2AuthenticationToken authentication) {
        System.out.println(authentication);
    }
}
