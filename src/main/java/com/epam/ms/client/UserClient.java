package com.epam.ms.client;

import com.epam.ms.service.calculator.model.UserProfile;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(value = "user-service", url = "${clients.user-service.url}", path = "/users")
public interface UserClient {

    //@LoadBalanced
    @RequestMapping(method = GET, value = "/{id}/profiles")
    UserProfile findProfileByUserId(@PathVariable String id);
}
