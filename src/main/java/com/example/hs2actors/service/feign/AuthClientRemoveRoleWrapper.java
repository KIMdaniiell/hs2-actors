package com.example.hs2actors.service.feign;

import com.example.hs2actors.controller.exceptions.fallback.DubControllerException;
import com.example.hs2actors.controller.exceptions.fallback.ServiceUnavailableException;
import com.example.hs2actors.controller.exceptions.not_found.UserNotFoundException;
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

        String errorMessage = exception.getMessage();

        if (null != exception.getCause()
                && exception.getCause().toString().equals("java.net.SocketTimeoutException: Connect timed out")) {
            throw new ServiceUnavailableException("Auth service is temporarily unavailable" +
                    " --- " + exception.getMessage());
        } else {

            int statusCode = Integer.parseInt(errorMessage.substring(
                    errorMessage.indexOf("[") + 1,
                    errorMessage.indexOf("]")
            ));
            String error;
            String message;

            if (errorMessage.contains("error") && errorMessage.contains("message")) {
                error = getField("error", errorMessage);
                message = "[" + statusCode + "] " + getField("message", errorMessage);
            } else {
                error = errorMessage.substring(errorMessage.lastIndexOf("[") + 1, errorMessage.lastIndexOf("]"));
                message = "---";
            }

            System.out.println("[statusCode] " + statusCode);
            System.out.println("[error] " + error);
            System.out.println("[message] " + message);

            switch (error) {
//                case "Action is unavailable":
//                    throw new UnavailableActionException(message);
//                case "Already applied action":
//                    throw new AlreadyAppliedException(message);
                case "Element Not Found":
                    throw new UserNotFoundException(message);
                default:
                    throw new DubControllerException(statusCode, error, message);
            }

        }

    }

    private static String getField(String field, String message) {
        String fullField = "\"" + field + "\":";
        int start = message.indexOf(fullField) + fullField.length() + 1;
        String postText = message.substring(start);
        int end = postText.indexOf("\"");
        return postText.substring(0, end);
    }
}
