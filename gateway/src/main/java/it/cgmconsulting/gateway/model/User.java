package it.cgmconsulting.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter @AllArgsConstructor @NoArgsConstructor @Getter
public class User {

    private long id;
    private String username;
    private String authorities;

}
