package com.epam.ms.user;


import com.epam.ms.user.dto.UserProfile;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserClient {
    @RequestLine("GET /{id}")
    UserProfile findById(@Param("id") String id);

    @RequestLine("GET")
    List<UserProfile> findByUserId();

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    void create(UserProfile userProfile);
}
