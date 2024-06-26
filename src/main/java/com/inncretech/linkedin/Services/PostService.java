package com.inncretech.linkedin.Services;

import com.inncretech.linkedin.DTOs.PostAddDto;
import com.inncretech.linkedin.DTOs.PostUpdateDto;
import com.inncretech.linkedin.Mappers.PostMapper;
import com.inncretech.linkedin.Models.*;
import com.inncretech.linkedin.Repository.CompanyFollowerRepository;
import com.inncretech.linkedin.Repository.CompanyRepository;
import com.inncretech.linkedin.Repository.PostsRepository;
import com.inncretech.linkedin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyFollowerRepository companyFollowerRepository;

    @Autowired
    private PostMapper postMapper;

    public List<PostAddDto> getAllPosts()
    {
        List<Posts> posts =  postsRepository.findAll();
        return posts.stream().
                filter(post -> !post.isDeleted())
                .map(post -> postMapper.postToPostAddDto(post))
                .collect(Collectors.toList());
    }

        public PostAddDto getPostById(Long id)
        {
            Optional<Posts> postFromDB = postsRepository.findById(id);
            if(postFromDB.isPresent() && !postFromDB.get().isDeleted()){
                Posts post = postFromDB.get();
                return postMapper.postToPostAddDto(post);
            }
            else {
                throw  new IllegalArgumentException("Post Not Found with Id " + id);
            }
        }

    public PostAddDto createPost(PostAddDto postAddDto)
    {
        Optional<Company> company = companyRepository.findById(postAddDto.getCompanyId());
        if(!company.isPresent() || company.get().isDeleted())
            throw new IllegalArgumentException("Company does not exists with the given Id");


        Optional<User> user = userRepository.findById(postAddDto.getUserId());
        if (!user.isPresent() || user.get().isDeleted()) {
            throw new IllegalArgumentException("User not found with id " + postAddDto.getUserId());
        }

        Optional<Company_followers> companyFollowers = companyFollowerRepository.findByCompanyIdAndUserId(company.get().getId(),user.get().getId());

        if(!companyFollowers.isPresent())
        {
            throw new NullPointerException("The user is not admin and can not create post");
        }
        if(!companyFollowers.get().getRole().getRoleName().equals("Admin"))
            throw new IllegalArgumentException("The user is not admin and can not create post");


        Posts posts = new Posts();
        posts.setCreatedAt(LocalDateTime.now());
        posts.setUpdatedAt(LocalDateTime.now());
        posts.setDeleted(false);
        posts.setContent(postAddDto.getContent());
        posts.setLikes(0);
        posts.setUser(user.get());

        postsRepository.save(posts);
        return postMapper.postToPostAddDto(posts);
    }

    public PostAddDto updatePost(Long id,PostUpdateDto postUpdateDto)
    {
        Optional<Posts> postsFromDB = postsRepository.findById(id);
        if(postsFromDB.isPresent() && !postsFromDB.get().isDeleted())
        {
            Posts post = postsFromDB.get();
            post.setContent(postUpdateDto.getContent());
            post.setUpdatedAt(LocalDateTime.now());
            postsRepository.save(post);
            return postMapper.postToPostAddDto(post);
        }
        else{
            throw new IllegalArgumentException("post not found");
        }
    }

    public void deletePost(Long id) {
        Optional<Posts> postFromDB = postsRepository.findById(id);
        if (postFromDB.isPresent() && !postFromDB.get().isDeleted()) {
            Posts post = postFromDB.get();
            post.setDeleted(true);
            post.setUpdatedAt(LocalDateTime.now());
            postsRepository.save(post);
        } else {
            throw new IllegalArgumentException("Post not found with id " + id);

        }
    }
}
