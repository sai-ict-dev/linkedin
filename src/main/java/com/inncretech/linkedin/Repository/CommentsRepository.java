package com.inncretech.linkedin.Repository;

import com.inncretech.linkedin.Models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments,Long> {

    List<Comments> findByPostId(Long id);

    Optional<Comments> findByUserIdAndPostId(Long userId, Long postId);
}
