package com.temitope.myblogapp.repository;

import com.temitope.myblogapp.enums.PostStatus;
import com.temitope.myblogapp.enums.UserRole;
import com.temitope.myblogapp.model.BlogPost;
import com.temitope.myblogapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Optional<BlogPost> findByIdAndStatusIn(Long id, List<PostStatus> statuses);

    Page<BlogPost> findByStatusInOrderByPublishedAtDesc(List<PostStatus> statuses, Pageable pageable);

    Page<BlogPost> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);

    @Query("SELECT bp FROM BlogPost bp WHERE bp.status = :status AND bp.author.role = :authorRole ORDER BY bp.createdAt ASC")
    Page<BlogPost> findPendingPostsByAuthorRole(@Param("status") PostStatus status, @Param("authorRole") UserRole authorRole, Pageable pageable);

    @Query("SELECT bp FROM BlogPost bp WHERE bp.status = :status AND bp.author.role = :authorRole ORDER BY bp.createdAt ASC")
    Page<BlogPost> findDeletePendingPostsByAuthorRole(@Param("status") PostStatus status, @Param("authorRole") UserRole authorRole, Pageable pageable);

}



