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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PactFolder("pacts")
@PactTestFor(providerName = "user_service", hostInterface = "localhost", port = "8081")
@Slf4j
public class UserServiceContractTest {
    private static final String USER_ID = "7fe1faf1-dce3-4502-965f-b3f3b0cedfe9";
    private static final String PROFILE_ID = "1e790666-d5d8-492b-8610-17fdc43798e6";
    @Autowired
    private UserClient userClient;
    @Autowired
    private ObjectMapper mapper;

    @Pact(consumer = "nutritionist_service", provider = "user_service")
    public RequestResponsePact createPactGet(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        UserProfile expected = new UserProfile(PROFILE_ID, 21, 160, 55, Goal.CUT, PhysicalActivity.ACTIVE_LIFESTYLE);
        UserProfile defaultProfile = createDefaultProfile();
        String expectedJSON = "";
        try{
            expectedJSON = mapper.writeValueAsString(expected);
        } catch (JsonProcessingException e) {
            log.error("Cannot process JSON");
        }

        return builder
                .given("test GET")
                .uponReceiving("GET REQUEST")
                .path(String.format("/users/%s/profiles", USER_ID))
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(expectedJSON)
                .toPact();
    }

    @Pact(consumer = "nutritionist_service", provider = "user_service")
    public RequestResponsePact createPactPost(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        UserProfile defaultProfile = createDefaultProfile();
        String defaultJSON = "";
        try{
            defaultJSON = mapper.writeValueAsString(defaultProfile);
        } catch (JsonProcessingException e) {

        }

        return builder
                .given("test POST")
                .uponReceiving("POST REQUEST")
                .method("POST")
                .headers(headers)
                .body(defaultJSON)
                .path("/users/profiles")
                .willRespondWith()
                .status(201)
                .toPact();
    }

    @org.junit.jupiter.api.Test
    @PactTestFor(pactMethod = "createPactPost")
    public void post_shouldCreateUser_ifNotExists() {
        UserProfile profile = userClient.findProfileByUserId(USER_ID);

        Assertions.assertEquals(PROFILE_ID, profile.getId());
    }

    private UserProfile createDefaultProfile() {
        UserProfile profile = new UserProfile();
        profile.setPhysicalActivity(PhysicalActivity.LOW_ACTIVITY);
        profile.setGoal(Goal.BODY_RELIEF);
        profile.setWeight(80);
        profile.setHeight(175);
        profile.setAge(25);
        return profile;
    }
}
