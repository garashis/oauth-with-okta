package com.example.oAuth2Login;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MyController.class)
public class MyControllerMVCTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenValidInput_thenReturns200() throws Exception {

        OidcUser oidcUser = new DefaultOidcUser(
                AuthorityUtils.createAuthorityList("SCOPE_message:read"),
                OidcIdToken.withTokenValue("id-token").claim("user_name", "foo_user").build(),
                "user_name");

        mockMvc.perform(get("/profile")
//                .with(oidcLogin().authorities(new SimpleGrantedAuthority("SCOPE_message:read")))
                        .with(oidcLogin().oidcUser(oidcUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}

