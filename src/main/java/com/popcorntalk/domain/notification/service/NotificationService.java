package com.popcorntalk.domain.notification.service;

import com.popcorntalk.domain.notification.controller.NotificationController;
import com.popcorntalk.domain.notification.entity.Notification;
import com.popcorntalk.domain.notification.repository.NotificationRepository;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.service.PostService;
import com.popcorntalk.domain.post.service.PostServiceImpl;
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

//    public void notifyComment(Long postId) {
//        Post post = postService.findPost(postId);
//        Comment receiveComment = commentService.findLatestComment(postId);
//        long userId = postService.findPost(postId).getUserId();
//
//        if (NotificationController.sseEmitters.containsKey(userId)) {
//            SseEmitter sseEmitter = NotificationController.sseEmitters.get(
//                userId);
//            try {
//                Map<String, String> eventData = new HashMap<>();
//                eventData.put("sender",
//                    receiveComment.getUser().getUsername() + " 님이 댓글을 작성했습니다.");
//                eventData.put("contents", receiveComment.getComment());
//
//                sseEmitter.send(SseEmitter.event().name("addComment").data(eventData));
//
//                Notification notification = Notification.builder()
//                    .userId(workerId)
//                    .sender(receiveComment.getNickname())
//                    .contents(receiveComment.getComment())
//                    .build();
//
//                Notification notification = new Notification();
//                notification.setPost(post);
//                notification.setSender(receiveComment.getUser().getUsername());
//                notification.setContents(receiveComment.getComment());
//                notificationRepository.save(notification);
//
//                notificationCounts.put(userId, notificationCounts.getOrDefault(userId, 0) + 1);
//
//                sseEmitter.send(SseEmitter.event().name("notificationCount")
//                    .data(notificationCounts.get(userId)));
//
//            } catch (IOException e) {
//                NotificationController.sseEmitters.remove(userId);
//            }
//        }
//    }
}
