package com.epam.ms.contract;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactFolder;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import com.epam.ms.client.UserClient;
import com.epam.ms.service.calculator.model.Goal;
import com.epam.ms.service.calculator.model.PhysicalActivity;
import com.epam.ms.service.calculator.model.UserProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PactFolder("pacts")
@PactTestFor(providerName = "user_service", hostInterface = "localhost", port = "8080")
@Slf4j
public class UserServiceContractTest {

    private static final String USER_ID = "7fe1faf1-dce3-4502-965f-b3f3b0cedfe9";
    private static final String PROFILE_ID = "1e790666-d5d8-492b-8610-17fdc43798e6";
    private UserProfile userProfile = new UserProfile(PROFILE_ID, 21, 160, 55, Goal.CUT, PhysicalActivity.ACTIVE_LIFESTYLE);

    @Autowired
    private UserClient userClient;
    @Autowired
    private ObjectMapper mapper;

    @Pact(consumer = "nutritionist_service", provider = "user_service")
    public RequestResponsePact getUserProfile(PactDslWithProvider builder) throws JsonProcessingException {
        String expectedJSON = mapper.writeValueAsString(userProfile);
        return builder
                .given("GET request for user profile is processed successfully")
                .uponReceiving("GET request to retrieve user profile")
                .path(format("/users/%s/profiles", USER_ID))
                .method("GET")

                .willRespondWith()
                .status(200)
                .headers(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .body(expectedJSON)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getUserProfile")
    public void get_shouldReturnProfileByUserId() {
        UserProfile profile = userClient.findProfileByUserId(USER_ID);
        assertEquals(userProfile, profile);
    }
}
