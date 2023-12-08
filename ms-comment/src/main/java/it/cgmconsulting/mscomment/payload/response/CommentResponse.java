package it.cgmconsulting.mscomment.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class CommentResponse {
    private long id;
    private String comment; // se censored = true va oscurato con asterischi
    LocalDateTime createdAt;
    private long authorId;
    private String author;

    public CommentResponse(long id, String comment, LocalDateTime createdAt, long authorId) {
        this.id = id;
        this.comment = comment;
        this.createdAt = createdAt;
        this.authorId = authorId;
    }
}
