package it.cgmconsulting.mscomment.service;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.payload.request.CommentRequest;
import it.cgmconsulting.mscomment.payload.response.CommentResponse;
import it.cgmconsulting.mscomment.payload.response.UserResponse;
import it.cgmconsulting.mscomment.repository.CommentRepository;
import it.cgmconsulting.mscomment.utils.EndPoints;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    public ResponseEntity<?> createComment(long authorId, CommentRequest request) {
        Comment comment = new Comment(request.getComment(),authorId, request.getPostId());
        commentRepository.save(comment);
        return new ResponseEntity<>("Comment successfully added", HttpStatus.CREATED);
    }

    public ResponseEntity<?> getComments(long postId) {

        List<CommentResponse> comments = commentRepository.getCommentsByPostId(postId);

        List<UserResponse> readers = getUsersByRole("ROLE_READER");

        comments.forEach(p -> {
            readers.stream()
                    .filter(u -> u.getId() == p.getAuthorId())
                    .findFirst()
                    .ifPresent(u -> p.setAuthor(u.getUsername()));

        });

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    public String getAuthor(long userId){
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = EndPoints.GATEWAY + EndPoints.MS_AUTH + "/v0/" + userId;
        try {
            return restTemplate.getForObject(resourceUrl, String.class);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }
    }


    public List<UserResponse> getUsersByRole(String role){

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = EndPoints.GATEWAY + EndPoints.MS_AUTH + "/v0/get-users-by-role/" + role;

        try {
            ParameterizedTypeReference<List<UserResponse>> type = new ParameterizedTypeReference<List<UserResponse>>() {};
            ResponseEntity<List<UserResponse>> listResponseEntity =
                    restTemplate.exchange(resourceUrl, HttpMethod.GET, null, type);
            return listResponseEntity.getBody();
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }
    }
}
