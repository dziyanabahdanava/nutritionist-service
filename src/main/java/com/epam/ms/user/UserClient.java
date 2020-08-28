package com.epam.ms.user;


import com.epam.ms.user.dto.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(value = "user-service", url = "https://localhost:8080", path = "/users")
public interface UserClient {

    @RequestMapping(method = GET, value = "/{id}/profile")
    UserProfile findProfileByUserId(@PathVariable String id);

    @RequestMapping(method = POST)
    void create(UserProfile userProfile);

}
