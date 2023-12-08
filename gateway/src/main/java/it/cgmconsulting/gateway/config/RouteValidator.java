package it.cgmconsulting.gateway.config;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {

   /* public static final List<String> openApiEndpoints = List.of(
            "/ms-auth/signup",
            "/ms-auth/signin",
            "/ms-post/"
    );*/

   /* public boolean isOpenEndpoint(ServerHttpRequest request){
        if(openApiEndpoints.contains(request.getURI().getPath()))
            return true;
        return false;
    }
    */
   // public static final String openApiEndpoint ="v0";

    public boolean isOpenEndpoint(ServerHttpRequest request){
        if(request.getURI().getPath().contains("v0") || request.getURI().getPath().contains("actuator"))
            return true;
        return false;
    }
}
