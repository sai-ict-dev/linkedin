package com.inncretech.linkedin.Controller;

import com.inncretech.linkedin.DTOs.CommentDto;
import com.inncretech.linkedin.Models.Comments;
import com.inncretech.linkedin.Services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/Comments")
public class CommentsController {


    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto)
    {
        CommentDto createdComment = commentService.createComment(commentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }



    @GetMapping("/getAllCommentsByPostId/{id}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable Long id)
    {
        List<CommentDto> comments = commentService.getCommentsByPostId(id);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

}
