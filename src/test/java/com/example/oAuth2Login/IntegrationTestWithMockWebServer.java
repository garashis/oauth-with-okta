package com.example.oAuth2Login;

import com.fasterxml.jackson.databind.ObjectMapper;
import mockwebserver3.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

//https://www.industriallogic.com/blog/better-integration-testing-with-mockwebserver/
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTestWithMockWebServer {
    public static MockWebServer mockServer;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("fakestoreapi", () -> mockServer.url("/").toString());
    }

    @Test
    void handlesSuccessResponseForGET() throws Exception {
        Employee employee = new Employee("fName");
        String responseBody = objectMapper.writeValueAsString(employee);
        mockExternalEndpoint(500, responseBody);
        mockExternalEndpoint(500, responseBody);
        mockExternalEndpoint(500, responseBody);
        mockExternalEndpoint(200, responseBody);

        ResultActions result = executeGetRequest();
        assertBackendServerWasCalledCorrectlyForGET(mockServer.takeRequest());
        assertBackendServerWasCalledCorrectlyForGET(mockServer.takeRequest());
        assertBackendServerWasCalledCorrectlyForGET(mockServer.takeRequest());
        assertBackendServerWasCalledCorrectlyForGET(mockServer.takeRequest());
        // verifyResults(result, 200, "\"alarm1\":\"Time to get up\"", "\"alarm2\":\"You're gonna be late\"");
    }

    private void assertBackendServerWasCalledCorrectlyForGET(RecordedRequest recordedRequest) {
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        assertThat(recordedRequest.getPath()).isEqualTo("/users");
    }

    private void mockExternalEndpoint(int responseCode, String body) {
        MockResponse mockResponse = new MockResponse().setResponseCode(responseCode)
                .setBody(body)
                .addHeader("Content-Type", "application/json");
        mockServer.enqueue(mockResponse);
    }


    private ResultActions executeGetRequest() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                        .get("/retryService")
                        .header("Identification-No", "app-id")
                        .header("Authorization", "Bearer 123"))
                .andDo(MockMvcResultHandlers.print());
    }
}
