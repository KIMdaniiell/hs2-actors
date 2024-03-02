package com.example.hs2actors.service.feign;

import com.example.hs2actors.controller.exceptions.fallback.ServiceUnavailableException;
import com.example.hs2actors.model.dto.RoleDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthClientRemoveRoleWrapper {

    private final AuthClient authClient;

    @CircuitBreaker(name = "RoleRemovalCircuitBreaker", fallbackMethod = "getFallback")
    public void removeRole(long id, RoleDTO roleDTO) {
        authClient.removeRole(id, roleDTO);
    }

    public void getFallback(Throwable exception) throws ServiceUnavailableException {
        throw new ServiceUnavailableException("Auth service is temporarily unavailable"
                + "\n" + exception.getMessage());
    }
}
