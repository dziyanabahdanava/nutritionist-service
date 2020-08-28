package com.epam.ms.user;

import com.epam.ms.user.dto.UserProfile;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserServiceMediator {
    public List<UserProfile> getUserProfile(String userId) {
        UserClient userClient = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(UserClient.class, "http://localhost:8080/user_profiles?userId=" + userId);
        log.info("Trying to get User Profile for: {}", userId);
        return userClient.findByUserId();
    }
}
