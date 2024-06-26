package com.inncretech.linkedin.Mappers;

import com.inncretech.linkedin.DTOs.PostAddDto;
import com.inncretech.linkedin.Models.Posts;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostAddDto postToPostAddDto(Posts posts)
    {
        if(posts == null)
            return null;
        PostAddDto postAddDto = new PostAddDto();
        postAddDto.setContent(posts.getContent());
        postAddDto.setUserId(posts.getUser().getId());
        postAddDto.setCompanyId(posts.getCompany().getId());

        return postAddDto;
    }
}
