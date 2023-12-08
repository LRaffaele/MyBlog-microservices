package it.cmqconsulting.msrating.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class Rating {


    //un utente può votare un post una volta sola al giorno
    // quindi in un anno un post può ottenere al max 365 per utente
    // PK: postId, userId, LocalDate

    @EmbeddedId
    @EqualsAndHashCode.Include
    private RatingId ratingId;

    private byte rate;



}
