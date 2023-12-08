package it.cmqconsulting.msrating.repository;

import it.cmqconsulting.msrating.entity.Rating;
import it.cmqconsulting.msrating.entity.RatingId;
import it.cmqconsulting.msrating.payload.response.AvgPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Query(value="SELECT COALESCE(ROUND(AVG(r.rate), 2), 0.0) FROM Rating r WHERE r.ratingId.postId = :postId")
    double getAvg(@Param("postId") long postId);


    @Query(value = "SELECT new it.cmqconsulting.msrating.payload.response.AvgPosts(" +
            "r.ratingId.postId, " +
            "AVG(r.rate) " +
            ") FROM Rating r " +
            "GROUP BY r.ratingId.postId")
    List<AvgPosts> getAvgPosts ();


}
