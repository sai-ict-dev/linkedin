package com.inncretech.linkedin.Services;

import com.inncretech.linkedin.Models.*;
import com.inncretech.linkedin.DTOs.*;
import com.inncretech.linkedin.Repository.*;
import com.inncretech.linkedin.Mappers.*;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.when;

public class PostServiceTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyFollowerRepository companyFollowerRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;

    private Long postId;

    private Posts posts;
    private PostAddDto postAddDto;
    private Long userId;
    private Long companyId;
    private Company company;
    private Roles roles;
    private User user;

    private Company_followers companyFollowers;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        companyId = 1L;
        company = new Company();
        company.setId(companyId);
        company.setName("test company");
        company.setProfile("profile");
        company.setDeleted(false);

        userId = 1L;
        user = new User();
        user.setId(userId);
        user.setFirstName("test first name");
        user.setLastName("test Last Name");
        user.setEmail("test@gmail.com");
        user.setPassword("test@123");
        user.setMobile(7422742611L);
        user.setDeleted(false);


        postId = 1L;
        posts = new Posts();
        posts.setId(postId);
        posts.setDeleted(false);
        posts.setContent("post content");
        posts.setUser(user);
        posts.setCompany(company);
        posts.setUpdatedAt(LocalDateTime.now());

        postAddDto = new PostAddDto();
        postAddDto.setUserId(userId);
        postAddDto.setCompanyId(companyId);

        roles = new Roles();
        roles.setId(1L);
        roles.setRoleName("Admin");

        companyFollowers = new Company_followers();
        companyFollowers.setId(1L);
        companyFollowers.setUser(user);
        companyFollowers.setCompany(company);
        companyFollowers.setRole(roles);
    }

    @Test
    public void testGetPostByIdValidPost() {

        when(postsRepository.findById(postId)).thenReturn(Optional.of(posts));
        when(postMapper.postToPostAddDto(posts)).thenReturn(postAddDto);


        PostAddDto result = postService.getPostById(postId);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, postAddDto);
    }

    @Test
    public void testGetPostByIdDeletedPost(){
        posts.setDeleted(true);

        when(postsRepository.findById(postId)).thenReturn(Optional.of(posts));
        try{
            PostAddDto result = postService.getPostById(postId);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Post Not Found with Id "+ postId);
        }
    }

    @Test
    public void testGetPostByIdInvalidPost(){
        when(postsRepository.findById(postId)).thenReturn(Optional.empty());
        try{
            PostAddDto result = postService.getPostById(postId);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Post Not Found with Id " + postId);
        }
    }


    @Test
    public void testCreatePostInvalidUser()
    {
        postAddDto.setUserId(null);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Mockito.verify(postsRepository,Mockito.never()).save(Mockito.any());
        try{
            PostAddDto result = postService.createPost(postAddDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"User not found with id "+postAddDto.getUserId());
        }
    }

    @Test
    public void testCreatePostInvalidCompany()
    {
        postAddDto.setCompanyId(null);
        try{
            PostAddDto result = postService.createPost(postAddDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company does not exists with the given Id");
        }
    }

    @Test
    public void testCreatePostUserNotAdmin()
    {
        companyFollowers.getRole().setRoleName("User");
        try{
            PostAddDto result = new PostAddDto();
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"The user is not admin and can not create post");
        }
    }

    @Test
    public void testCreatePostInvalidCompanyFollower()
    {
        companyFollowers.setUser(null);
        try{
            PostAddDto result = new PostAddDto();
        }
        catch (NullPointerException ex)
        {
            Assert.assertEquals(ex.getMessage(),"The user is not admin and can not create post");
        }
    }

    @Test
    public void testCreatePostValidPost()
    {
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(companyFollowerRepository.findByCompanyIdAndUserId(companyId, userId)).thenReturn(Optional.of(companyFollowers));
        when(postsRepository.save(Mockito.any(Posts.class))).thenReturn(posts);
        when(postMapper.postToPostAddDto(Mockito.any(Posts.class))).thenReturn(postAddDto);
        PostAddDto result = postService.createPost(postAddDto);


        Assert.assertNotNull(result);
        Assert.assertEquals(result, postAddDto);

    }

    @Test
    public void testUpdatePostValidData()
    {
        PostUpdateDto postUpdateDto = new PostUpdateDto();
        postUpdateDto.setContent("updated content");

        when(postsRepository.findById(postId)).thenReturn(Optional.of(posts));
        when(postsRepository.save(Mockito.any(Posts.class))).thenReturn(posts);
        when(postMapper.postToPostAddDto(posts)).thenReturn(postAddDto);

        PostAddDto updatedPost = postService.updatePost(postId,postUpdateDto);

        Assert.assertNotNull(updatedPost);
        Assert.assertEquals(posts.getContent(),"updated content");
    }

    @Test
    public void testUpdatePostInvalidPostId()
    {
        PostUpdateDto postUpdateDto = new PostUpdateDto();
        postUpdateDto.setContent("updated content");

        when(postsRepository.findById(postId)).thenReturn(Optional.empty());

        try{
            PostAddDto result = postService.updatePost(postId,postUpdateDto);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"post not found");
        }
    }

    @Test
    public void testUpdatePostDeletedPost()
    {
        posts.setDeleted(true);
        PostUpdateDto postUpdateDto = new PostUpdateDto();
        postUpdateDto.setContent("updated content");
        when(postsRepository.findById(postId)).thenReturn(Optional.of(posts));

        try{
            postService.updatePost(postId,postUpdateDto);
        }
        catch (IllegalArgumentException ex){
            Assert.assertEquals(ex.getMessage(),"post not found");
        }
    }

    @Test
    public void testDeletePostValidId()
    {
        posts.setDeleted(false);
        when(postsRepository.findById(postId)).thenReturn(Optional.of(posts));
        when(postsRepository.save(posts)).thenReturn(posts);

        postService.deletePost(postId);

        Assert.assertNotNull(posts);
        Assert.assertTrue(posts.isDeleted());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDeletePostInvalidId()
    {
        when(postsRepository.findById(postId)).thenReturn(Optional.empty());

        postService.deletePost(postId);

    }

    @Test
    public void testDeletePostDeletedId()
    {
        posts.setDeleted(true);
        when(postsRepository.findById(postId)).thenReturn(Optional.of(posts));

        try{
            postService.deletePost(postId);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Post not found with id "+ postId);
        }
    }

    public void setData(){

    }

    @Test
    public void testGetAllPostsAllNonDeletedPosts()
    {
        Posts posts1 = new Posts();
        posts1.setDeleted(false);

        Posts posts2 = new Posts();
        posts2.setDeleted(false);

        Posts posts3 = new Posts();
        posts3.setDeleted(false);

        PostAddDto dto1 = new PostAddDto();
        PostAddDto dto2 = new PostAddDto();
        PostAddDto dto3 = new PostAddDto();


        List<Posts> postsList = Arrays.asList(posts1,posts2,posts3);

        when(postsRepository.findAll()).thenReturn(postsList);

        when(postMapper.postToPostAddDto(posts1)).thenReturn(dto1);
        when(postMapper.postToPostAddDto(posts2)).thenReturn(dto2);
        when(postMapper.postToPostAddDto(posts3)).thenReturn(dto3);

        List<PostAddDto> result =  postService.getAllPosts();

        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(),3);
        Assert.assertTrue(result.contains(dto1));
        Assert.assertTrue(result.contains(dto2));
        Assert.assertTrue(result.contains(dto3));
    }

    @Test
    public void testGetAllPostsAllDeletedPosts()
    {
        Posts posts1 = new Posts();
        posts1.setDeleted(true);

        Posts posts2 = new Posts();
        posts2.setDeleted(true);

        Posts posts3 = new Posts();
        posts3.setDeleted(true);

        PostAddDto dto1 = new PostAddDto();
        PostAddDto dto2 = new PostAddDto();
        PostAddDto dto3 = new PostAddDto();


        List<Posts> postsList = Arrays.asList(posts1,posts2,posts3);

        when(postsRepository.findAll()).thenReturn(postsList);

        when(postMapper.postToPostAddDto(posts1)).thenReturn(dto1);
        when(postMapper.postToPostAddDto(posts2)).thenReturn(dto2);
        when(postMapper.postToPostAddDto(posts3)).thenReturn(dto3);

        List<PostAddDto> result =  postService.getAllPosts();

        Assert.assertTrue(result.isEmpty());

    }




}
