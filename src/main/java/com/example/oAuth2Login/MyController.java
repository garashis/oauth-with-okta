package com.example.oAuth2Login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@Slf4j
public class MyController implements BaseController {
    private static void revokeToken(String accessToken, String clientId, String clientSecret) throws IOException {

        //POST Request to Twitch endpoint
        URL url = new URL("https://auth.pingone.eu/3981b07b-51ea-4e68-a152-4c9f539dc2dd/as/revoke");
        HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
        https.setRequestMethod("POST");
        https.setDoOutput(true);
        https.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String userCredentials = clientId + ":" + clientSecret;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));


        https.setRequestProperty("Authorization", basicAuth);

        //String data = "token_type_hint=access_token&token=" + accessToken;
        String data = "token_type_hint=refresh_token&token=" + accessToken;

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = https.getOutputStream();
        stream.write(out);
        System.out.println(https.getResponseCode() + " >>>>>>> " + stream);
    }

    //@GetMapping(value = {"/loginSuccess"})
    public String welcome(OAuth2AuthenticationToken authentication, @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient authorizedClient) {
        System.out.println(">>>>>>>> " + authentication.getPrincipal().getAuthorities());
        return "Hello " + authentication.getPrincipal().getAttribute("name");
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/login")
    public String login() {
        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public ModelAndView userDetails(OAuth2AuthenticationToken authentication,
                                    @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        log.info(authentication.toString());
        System.out.println("authentication.getAuthorities() : " + authentication.getAuthorities());
        logging(authentication);


/*        try {
            revokeToken(authorizedClient.getRefreshToken().getTokenValue(),
                    authorizedClient.getClientRegistration().getClientId(),
                    authorizedClient.getClientRegistration().getClientSecret()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


        return new ModelAndView("profile", Collections.singletonMap("details", authentication.getPrincipal().getAttributes()));
    }

    //@GetMapping("/")
    public String home(HttpServletRequest request, HttpServletResponse response) {

// Logic to invalidate Session

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

//to clear the Authentication
        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);


//Logic to invalidate cookie by setting age to 0
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equalsIgnoreCase("JSESSIONID")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }


        SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "home";
    }
}
