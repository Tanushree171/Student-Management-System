package com.university.sms.repository;

import com.university.sms.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByIsActiveTrueOrderByCreatedAtDesc();

    @Query("SELECT a FROM Announcement a WHERE a.isActive = true AND " +
           "(a.targetRole IS NULL OR a.targetRole = :role) " +
           "ORDER BY a.createdAt DESC")
    Page<Announcement> findActiveByRole(@Param("role") String role, Pageable pageable);

    List<Announcement> findByCategory(String category);
    long countByIsActiveTrue();
}
