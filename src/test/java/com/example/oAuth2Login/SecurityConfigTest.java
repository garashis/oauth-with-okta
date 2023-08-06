package com.example.oAuth2Login;

import org.junit.Test;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SecurityConfig.class, TestConfig.class})
@WebAppConfiguration()
public class SecurityConfigTest {

    //    @MockBean
//    OAuth2AuthorizedClientService service;
//    @MockBean
//    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        SecurityConfig securityConfig = context.getBean(SecurityConfig.class);

        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    @Test
    public void filterChain() throws Exception {
        mvc.perform(get("/profile")
                        //.with(oidcLogin().authorities(new SimpleGrantedAuthority("SCOPE_message:read")))
                        //.with(oidcLogin().oidcUser(oidcUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }
}

@TestConfiguration
class TestConfig {

    @Mock
    LogoutConfigurer logoutConfigurer;
    @Autowired
    private WebApplicationContext context;

    @Bean
    public OAuth2AuthorizedClientService getOAuth2AuthorizedClientService() {
        SecurityConfig securityConfig = context.getBean(SecurityConfig.class);
        securityConfig.logoutConfigurer = logoutConfigurer;
        return Mockito.mock(OAuth2AuthorizedClientService.class);
    }

    @Bean
    public ClientRegistrationRepository getClientRegistrationRepository() {
        return Mockito.mock(ClientRegistrationRepository.class);
    }
}
