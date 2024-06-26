package com.inncretech.linkedin.Mappers;

import com.inncretech.linkedin.DTOs.CommentDto;
import com.inncretech.linkedin.Models.Comments;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public CommentDto commentToCommentDto(Comments comments)
    {
        CommentDto commentDto = new CommentDto();
        commentDto.setComment(comments.getMessage());
        commentDto.setPostId(comments.getPost().getId());
        commentDto.setUserId(comments.getUser().getId());

        return commentDto;
    }

    public Comments CommentDtoToComment(CommentDto commentDto)
    {
        Comments comments = new Comments();
        comments.setMessage(commentDto.getComment());
        comments.setDeleted(false);
        comments.setCreatedAt(LocalDateTime.now());
        comments.setUpdatedAt(LocalDateTime.now());

        return comments;
    }
}
