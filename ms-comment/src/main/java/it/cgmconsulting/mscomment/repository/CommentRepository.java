package it.cgmconsulting.mscomment.repository;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {



   @Query(value = "SELECT new it.cgmconsulting.mscomment.payload.response.CommentResponse(" +
           "c.id, " +
           "CASE WHEN (c.censored = true) THEN '***************' ELSE c.comment END," +
           "c.createdAt, " +
           "c.authorId " +
           ") FROM Comment c " +
           "WHERE c.id = :postId " +
           "ORDER BY c.createdAt DESC"
           )
    List<CommentResponse> getCommentsByPostId(long postId);
}
