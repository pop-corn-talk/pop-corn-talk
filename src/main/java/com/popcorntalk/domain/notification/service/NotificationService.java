package com.popcorntalk.domain.notification.service;

import com.popcorntalk.domain.comment.entity.Comment;
import com.popcorntalk.domain.comment.service.CommentService;
import com.popcorntalk.domain.notification.controller.NotificationController;
import com.popcorntalk.domain.notification.entity.Notification;
import com.popcorntalk.domain.notification.repository.NotificationRepository;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.service.PostServiceImpl;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.repository.UserRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PostServiceImpl postService;
    private final CommentService commentService;
    private final UserRepository userRepository;
    private static Map<Long, Integer> notificationCounts = new HashMap<>();

    @Transactional
    public SseEmitter subscribe(Long userId) {

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationController.sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));

        return sseEmitter;
    }

    public void notifyComment(Long postId) {
        Post post = postService.findPost(postId);
        Comment receiveComment = commentService.findLatestComment(postId);
        User commentUser = userRepository.findById(receiveComment.getUserId()).orElseThrow(
            () -> new IllegalArgumentException("User not found")
        );

        long userId = post.getUserId();

        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                Map<String, String> eventData = new HashMap<>();
                eventData.put("sender", commentUser.getEmail() + " 님이 댓글을 작성했습니다.");
                eventData.put("contents", receiveComment.getContent());
                eventData.put("time", String.valueOf(receiveComment.getCreatedAt()));

                sseEmitter.send(SseEmitter.event().name("addComment").data(eventData));

                Notification notification = Notification.createOf(
                    post.getUserId(), commentUser.getEmail(), receiveComment.getContent()
                );
                notificationRepository.save(notification);

                notificationCounts.put(userId, notificationCounts.getOrDefault(userId, 0) + 1);

                sseEmitter.send(SseEmitter.event().name("notificationCount")
                    .data(notificationCounts.get(userId)));

            } catch (IOException e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }
}
