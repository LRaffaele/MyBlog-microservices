package it.cgmconsulting.mscomment.controller;

import it.cgmconsulting.mscomment.payload.request.CommentRequest;
import it.cgmconsulting.mscomment.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping("v3")
    public ResponseEntity<?> createComment(@RequestHeader("userId") @Min(1) long authorId, @RequestBody @Valid CommentRequest request){
        return commentService.createComment(authorId, request);
    }

    @GetMapping("v0/{postId}")
    public ResponseEntity<?> getComments (@PathVariable long postId){
        return commentService.getComments(postId);
    }
}
