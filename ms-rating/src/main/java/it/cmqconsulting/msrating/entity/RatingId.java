package it.cmqconsulting.msrating.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@AllArgsConstructor @Getter @NoArgsConstructor @Setter @EqualsAndHashCode
public class RatingId implements Serializable {


    private long postId;
    private long userId;


    @Column(updatable = false, nullable = false)
    private LocalDate votedAt = LocalDate.now();

    public RatingId(long postId, long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
