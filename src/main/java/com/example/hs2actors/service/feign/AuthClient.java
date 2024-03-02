package com.example.hs2actors.service.feign;

import com.example.hs2actors.model.dto.RoleDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @PutMapping("feign/users/{userId}/roles")
    void addRole(@PathVariable long userId, @RequestBody RoleDTO roleDTO);

    @DeleteMapping("feign/users/{userId}/roles")
    void removeRole(@PathVariable long userId, @Valid @RequestBody RoleDTO roleDTO);
}
