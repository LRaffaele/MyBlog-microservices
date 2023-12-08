package it.cmqconsulting.msrating.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @NoArgsConstructor @Setter @AllArgsConstructor
public class AvgPosts {

    private long postId;
    private double avg;

}
