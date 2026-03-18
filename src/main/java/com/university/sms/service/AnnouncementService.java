package com.university.sms.service;

import com.university.sms.entity.*;
import com.university.sms.exception.ResourceNotFoundException;
import com.university.sms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    public Page<Announcement> getAnnouncementsForRole(String role, Pageable pageable) {
        return announcementRepository.findActiveByRole(role, pageable);
    }

    public List<Announcement> getAllActive() {
        return announcementRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }

    public Announcement getById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement", "id", id));
    }

    public Announcement create(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    public Announcement update(Long id, Announcement updated) {
        Announcement existing = getById(id);
        existing.setTitle(updated.getTitle());
        existing.setBody(updated.getBody());
        existing.setCategory(updated.getCategory());
        existing.setTargetRole(updated.getTargetRole());
        existing.setTargetProgram(updated.getTargetProgram());
        existing.setFileUrl(updated.getFileUrl());
        existing.setScheduledAt(updated.getScheduledAt());
        return announcementRepository.save(existing);
    }

    public void archive(Long id) {
        Announcement a = getById(id);
        a.setIsActive(false);
        announcementRepository.save(a);
    }

    public void delete(Long id) {
        announcementRepository.deleteById(id);
    }

    public long getActiveCount() {
        return announcementRepository.countByIsActiveTrue();
    }
}
