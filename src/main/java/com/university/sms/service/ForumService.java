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
public class ForumService {

    private final ForumThreadRepository threadRepository;
    private final ForumReplyRepository replyRepository;
    private final UserRepository userRepository;

    public Page<ForumThread> getThreads(Pageable pageable) {
        return threadRepository.findAllByOrderByIsPinnedDescCreatedAtDesc(pageable);
    }

    public ForumThread getThreadById(Long id) {
        return threadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ForumThread", "id", id));
    }

    public ForumThread createThread(String title, String body, String category, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", authorId));

        ForumThread thread = ForumThread.builder()
                .title(title)
                .body(body)
                .category(category)
                .author(author)
                .build();

        return threadRepository.save(thread);
    }

    public ForumReply addReply(Long threadId, String body, Long authorId) {
        ForumThread thread = getThreadById(threadId);
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", authorId));

        ForumReply reply = ForumReply.builder()
                .thread(thread)
                .author(author)
                .body(body)
                .upvotes(0)
                .build();

        return replyRepository.save(reply);
    }

    public List<ForumReply> getReplies(Long threadId) {
        return replyRepository.findByThreadIdOrderByCreatedAtAsc(threadId);
    }

    public void upvoteReply(Long replyId) {
        ForumReply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("ForumReply", "id", replyId));
        reply.setUpvotes(reply.getUpvotes() + 1);
        replyRepository.save(reply);
    }

    public Page<ForumThread> searchThreads(String keyword, Pageable pageable) {
        return threadRepository.searchByKeyword(keyword, pageable);
    }

    // Moderation
    public void flagThread(Long threadId) {
        ForumThread thread = getThreadById(threadId);
        thread.setIsFlagged(true);
        threadRepository.save(thread);
    }

    public void pinThread(Long threadId) {
        ForumThread thread = getThreadById(threadId);
        thread.setIsPinned(!thread.getIsPinned());
        threadRepository.save(thread);
    }

    public void deleteThread(Long threadId) {
        replyRepository.deleteAll(replyRepository.findByThreadIdOrderByCreatedAtAsc(threadId));
        threadRepository.deleteById(threadId);
    }
}
