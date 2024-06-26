package com.inncretech.linkedin.Services;
import com.inncretech.linkedin.Models.*;
import com.inncretech.linkedin.DTOs.*;
import com.inncretech.linkedin.Repository.*;
import com.inncretech.linkedin.Mappers.*;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class CommentServiceTest
{
    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private CommentService commentService;

    private Long userId;
    private Long postId;
    private Long companyId;
    private Long commentId;
    private String email;

    private User user;
    private Posts posts;
    private Company company;
    private Comments comments;

    private CommentDto commentDto;

    @BeforeMethod
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        user = new User();
        userId = 1L;
        email = "testinn@gmail.com";
        user.setId(userId);
        user.setFirstName("test first name");
        user.setLastName("test Last Name");
        user.setMobile(7862848232L);
        user.setEmail(email);
        user.setPassword("test@456");
        user.setDeleted(false);

        company = new Company();
        companyId = 1L;
        company.setId(companyId);
        company.setName("random company");
        company.setProfile("random profile");


        posts = new Posts();
        postId = 1L;
        posts.setId(postId);
        posts.setDeleted(false);
        posts.setContent("post content");
        posts.setUser(user);
        posts.setCompany(company);
        posts.setUpdatedAt(LocalDateTime.now());

        comments = new Comments();
        commentId = 1L;
        comments.setPost(posts);
        comments.setMessage("Post comment");
        comments.setId(commentId);
        comments.setUser(user);
    }

    @DataProvider(name = "commentDtoValidData")
    public Object[] commentDtoValidData()
    {
        postId = 1L;
        userId = 1L;
        commentDto = new CommentDto();
        commentDto.setComment("Post comment");
        commentDto.setPostId(postId);
        commentDto.setUserId(userId);
        return new Object[]{commentDto};
    }

    @Test(dataProvider = "commentDtoValidData")
    public void testCreateCommentValidData(CommentDto commentDto)
    {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postsRepository.findById(postId)).thenReturn(Optional.of(posts));
        when(commentsRepository.findByUserIdAndPostId(userId,postId)).thenReturn(Optional.empty());
        when(commentMapper.CommentDtoToComment(commentDto)).thenReturn(comments);
        when(commentsRepository.save(comments)).thenReturn(comments);
        when(commentMapper.commentToCommentDto(comments)).thenReturn(commentDto);

        CommentDto result = commentService.createComment(commentDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,commentDto);

    }

    @Test(dataProvider = "commentDtoValidData")
    public void testCreateCommentInvalidUser(CommentDto commentDto)
    {
        commentDto.setUserId(99L);
        when(userRepository.findById(commentDto.getUserId())).thenReturn(Optional.empty());
        try{
            CommentDto result = commentService.createComment(commentDto);
        }
        catch (Exception ex)
        {
            Assert.assertEquals(ex.getMessage(),"User Not Found with the Id " + commentDto.getUserId());
        }
    }

    @Test(dataProvider = "commentDtoValidData")
    public void testCreateCommentDeletedUser(CommentDto commentDto)
    {
        user.setDeleted(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        try{
            CommentDto result = commentService.createComment(commentDto);
        }
        catch (Exception ex)
        {
            Assert.assertEquals(ex.getMessage(),"User Not Found with the Id " + commentDto.getUserId());
        }
    }

    @Test(dataProvider = "commentDtoValidData")
    public void testCreateCommentInvalidPostId(CommentDto commentDto)
    {
        commentDto.setPostId(99L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postsRepository.findById(commentDto.getPostId())).thenReturn(Optional.empty());
        try{
            CommentDto result = commentService.createComment(commentDto);
        }
        catch (Exception ex)
        {
            Assert.assertEquals(ex.getMessage(),"Post Not Found with the Id " + commentDto.getPostId());
        }
    }

    @Test(dataProvider = "commentDtoValidData")
    public void testCreateCommentDeletedPost(CommentDto commentDto)
    {
        posts.setDeleted(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postsRepository.findById(commentDto.getPostId())).thenReturn(Optional.of(posts));
        try{
            CommentDto result = commentService.createComment(commentDto);
        }
        catch (Exception ex)
        {
            Assert.assertEquals(ex.getMessage(),"Post Not Found with the Id " + commentDto.getPostId());
        }
    }

    @Test(dataProvider = "commentDtoValidData")
    public void testCreateCommentExistingComment(CommentDto commentDto)
    {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postsRepository.findById(commentDto.getPostId())).thenReturn(Optional.of(posts));
        when(commentsRepository.findByUserIdAndPostId(userId,postId)).thenReturn(Optional.of(comments));
        try{
            CommentDto result = commentService.createComment(commentDto);
        }
        catch (Exception ex)
        {
            Assert.assertEquals(ex.getMessage(),"User already commented to the post");
        }
    }

    @Test
    public void testGetCommentByPostIdValidId()
    {
        List<Comments> commentsList = Arrays.asList(comments);
        when(postsRepository.findById(postId)).thenReturn(Optional.of(posts));
        when(commentsRepository.findByPostId(postId)).thenReturn(commentsList);
        when(commentMapper.commentToCommentDto(comments)).thenReturn(commentDto);

        List<CommentDto> result = commentService.getCommentsByPostId(postId);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains(commentDto));
    }

    @Test
    public void testGetCommentByPostIdInvalidId()
    {
        Long testPostId = 9999L;
        List<Comments> commentsList = Arrays.asList(comments);
        when(postsRepository.findById(postId)).thenReturn(Optional.empty());

        try{
        List<CommentDto> result = commentService.getCommentsByPostId(testPostId);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Post Not Found");
        }
    }

    @Test
    public void testGetCommentByPostIdDeletedPost()
    {
        posts.setDeleted(true);
        List<Comments> commentsList = Arrays.asList(comments);
        when(postsRepository.findById(postId)).thenReturn(Optional.of(posts));

        try{
            List<CommentDto> result = commentService.getCommentsByPostId(postId);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Post Not Found");
        }
    }


}
