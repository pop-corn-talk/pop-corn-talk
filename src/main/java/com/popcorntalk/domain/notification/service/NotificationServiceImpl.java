package com.popcorntalk.domain.notification.service;

import com.popcorntalk.domain.comment.entity.Comment;
import com.popcorntalk.domain.notification.controller.NotificationController;
import com.popcorntalk.domain.notification.entity.Notification;
import com.popcorntalk.domain.notification.repository.NotificationRepository;
import com.popcorntalk.domain.user.entity.User;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private static Map<Long, Integer> notificationCounts = new HashMap<>();

    @Override
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

    @Override
    @Transactional
    public void notifyComment(Long postUserId, User user, Comment receiveComment) {

        if (NotificationController.sseEmitters.containsKey(postUserId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(postUserId);
            try {
                Map<String, String> eventData = new HashMap<>();
                eventData.put("sender", user.getEmail() + " 님이 댓글을 작성했습니다.");
                eventData.put("contents", receiveComment.getContent());
                eventData.put("time", String.valueOf(receiveComment.getCreatedAt()));

                sseEmitter.send(SseEmitter.event().name("addComment").data(eventData));

                Notification notification = Notification.createOf(
                    postUserId, user.getEmail(), receiveComment.getContent()
                );
                notificationRepository.save(notification);

                notificationCounts.put(postUserId,
                    notificationCounts.getOrDefault(postUserId, 0) + 1);

                sseEmitter.send(SseEmitter.event().name("notificationCount")
                    .data(notificationCounts.get(postUserId)));

            } catch (IOException e) {
                NotificationController.sseEmitters.remove(postUserId);
            }
        }
    }

    @Override
    @Transactional
    public void notifyPurchase(Long recipientId, String sender, String productVoucherImage) {

        if (NotificationController.sseEmitters.containsKey(recipientId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(recipientId);
            try {
                Map<String, String> eventData = new HashMap<>();
                eventData.put("sender", sender + " 님이 주문하신 상품을 보냈습니다.");
                eventData.put("contents", productVoucherImage);
                eventData.put("time", String.valueOf(LocalDateTime.now()));

                sseEmitter.send(SseEmitter.event().name("sendProduct").data(eventData));

                Notification notification = Notification.createOf(
                    recipientId, sender, productVoucherImage
                );
                notificationRepository.save(notification);

                notificationCounts.put(recipientId,
                    notificationCounts.getOrDefault(recipientId, 0) + 1);

                sseEmitter.send(SseEmitter.event().name("notificationCount")
                    .data(notificationCounts.get(recipientId)));

            } catch (IOException e) {
                NotificationController.sseEmitters.remove(recipientId);
            }
        }
    }
}
