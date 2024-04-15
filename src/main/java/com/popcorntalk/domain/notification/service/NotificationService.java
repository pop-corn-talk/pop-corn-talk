package com.popcorntalk.domain.notification.service;

import com.popcorntalk.domain.comment.entity.Comment;
import com.popcorntalk.domain.user.entity.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    /**
     * 알림 서비스 구독 및 알림을 전송
     *
     * @param userId 구독할 유저의 ID
     * @return SSEEmitter(알림) 객체
     */
    SseEmitter subscribe(Long userId);

    /**
     * 댓글 작성 시 알림 전송
     *
     * @param postUserId     받는 사람
     * @param user           보낸 사람
     * @param receiveComment 댓글
     */
    void notifyComment(Long postUserId, User user, Comment receiveComment);

    /**
     * 상품 구매 시 알림 전송 및 상품 전달
     *
     * @param recipientId         받는 수취인 Id
     * @param sender              보낸 사람
     * @param productVoucherImage 구매한 상품
     */
    void notifyPurchase(Long recipientId, String sender, String productVoucherImage);
}
