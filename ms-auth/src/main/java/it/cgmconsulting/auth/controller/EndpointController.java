package it.cgmconsulting.auth.controller;

import it.cgmconsulting.auth.payload.response.UserResponse;
import it.cgmconsulting.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8090")
public class EndpointController {

    private final AuthService authService;

    @GetMapping("/v0/{userId}")
    public String getUsername(@PathVariable long userId){
        return authService.getUsername(userId);
    }

    @GetMapping("/v0/get-users-by-role/{role}")
    public List<UserResponse> getUsersByRole(@PathVariable String role){
        return authService.getUsersByRole(role);
    }

}
