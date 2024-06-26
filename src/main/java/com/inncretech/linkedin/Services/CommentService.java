package com.inncretech.linkedin.Services;

import com.inncretech.linkedin.DTOs.CommentDto;
import com.inncretech.linkedin.Mappers.CommentMapper;
import com.inncretech.linkedin.Models.*;
import com.inncretech.linkedin.Repository.CommentsRepository;
import com.inncretech.linkedin.Repository.PostsRepository;
import com.inncretech.linkedin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CommentMapper commentMapper;


    public List<CommentDto> getCommentsByPostId(Long id)
    {
        Optional<Posts> postFromDB = postsRepository.findById(id);
        if(postFromDB.isPresent() && !postFromDB.get().isDeleted())
        {
            List<Comments> comments = commentsRepository.findByPostId(id);

            return comments.stream()
                    .map(commentMapper::commentToCommentDto)
                    .collect(Collectors.toList());
        }

        else {
            throw new IllegalArgumentException("Post Not Found");
        }
    }


    public CommentDto createComment(CommentDto commentDto)
    {
        Optional<User> user = userRepository.findById(commentDto.getUserId());
        if(!user.isPresent() || user.get().isDeleted())
        {
            throw new IllegalArgumentException("User Not Found with the Id " + commentDto.getUserId());
        }

        Optional<Posts> posts = postsRepository.findById(commentDto.getPostId());
        if(!posts.isPresent() || posts.get().isDeleted())
        {
            throw new IllegalArgumentException("Post Not Found with the Id " + commentDto.getPostId());
        }

        Optional<Comments> commentsFromDB = commentsRepository.
                findByUserIdAndPostId(user.get().getId(),posts.get().getId());
        if(commentsFromDB.isPresent() && !commentsFromDB.get().isDeleted())
        {
            throw new IllegalArgumentException("User already commented to the post");
        }

        Comments comments = commentMapper.CommentDtoToComment(commentDto);
        comments.setPost(posts.get());
        comments.setUser(user.get());
        commentsRepository.save(comments);
        return commentMapper.commentToCommentDto(comments);
    }
}
