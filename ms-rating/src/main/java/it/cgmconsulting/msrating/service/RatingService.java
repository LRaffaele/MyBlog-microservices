package it.cmqconsulting.msrating.service;

import it.cmqconsulting.msrating.entity.Rating;
import it.cmqconsulting.msrating.entity.RatingId;
import it.cmqconsulting.msrating.payload.response.AvgPosts;
import it.cmqconsulting.msrating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public ResponseEntity<?> addRate(long postId, long userId, byte rate) {
        RatingId ratingId = new RatingId(postId,userId);
        if(ratingRepository.existsById(ratingId))
            return new ResponseEntity<>("User already voted today", HttpStatus.FORBIDDEN);

        Rating rating = new Rating(ratingId, rate);
        ratingRepository.save(rating);

        return new ResponseEntity<>("Rating added successfully", HttpStatus.OK);
    }

    public double getAvgByPost(long postId) {
        return ratingRepository.getAvg(postId);
    }


    public List<AvgPosts> getAvgPost(){
        return ratingRepository.getAvgPosts();
    }
}
