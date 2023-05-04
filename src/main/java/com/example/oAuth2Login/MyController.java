package com.example.oAuth2Login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@Controller
@Slf4j
public class MyController implements BaseController {
    //@GetMapping(value = {"/loginSuccess"})
    public String welcome(OAuth2AuthenticationToken authentication, @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient authorizedClient) {
        System.out.println(">>>>>>>> " + authentication.getPrincipal().getAuthorities());
        return "Hello " + authentication.getPrincipal().getAttribute("name");
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/profile")
    public ModelAndView userDetails(OAuth2AuthenticationToken authentication) {
        log.info(authentication.toString());
        logging(authentication);
        return new ModelAndView("profile", Collections.singletonMap("details", authentication.getPrincipal().getAttributes()));
    }
}
