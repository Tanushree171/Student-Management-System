package com.university.sms.repository;

import com.university.sms.entity.ForumThread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {
    Page<ForumThread> findAllByOrderByIsPinnedDescCreatedAtDesc(Pageable pageable);
    List<ForumThread> findByCategory(String category);
    List<ForumThread> findByAuthorId(Long authorId);
    List<ForumThread> findByIsFlaggedTrue();

    @Query("SELECT t FROM ForumThread t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.body) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ForumThread> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
