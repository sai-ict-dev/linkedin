package com.inncretech.linkedin.Controller;

import com.inncretech.linkedin.DTOs.PostAddDto;
import com.inncretech.linkedin.DTOs.PostUpdateDto;
import com.inncretech.linkedin.Services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postsService;

    @GetMapping("/all")
    public ResponseEntity<List<PostAddDto>> getAllPosts() {
        List<PostAddDto> posts = postsService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostAddDto> getPostById(@PathVariable Long id) {
        PostAddDto post = postsService.getPostById(id);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<PostAddDto> createPost(@Valid @RequestBody PostAddDto postsDto) {
        PostAddDto createdPost = postsService.createPost(postsDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostAddDto> updatePost(@PathVariable Long id, @RequestBody PostUpdateDto postUpdateDto) {
        PostAddDto updatedPost = postsService.updatePost(id, postUpdateDto);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postsService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
