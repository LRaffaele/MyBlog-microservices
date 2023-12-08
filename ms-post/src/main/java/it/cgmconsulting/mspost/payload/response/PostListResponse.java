package it.cgmconsulting.mspost.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PostListResponse {

    //ordinati per pubblicazione DESC
    private long id;
    private String title;
    private String overview;
}
