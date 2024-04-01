package com.popcorntalk.domain.post.service;

import com.popcorntalk.domain.post.entity.PostRecode;
import com.popcorntalk.domain.post.repository.PostRecodeRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRecodeServiceImpl implements PostRecodeService {

    private final PostRecodeRepository postRecodeRepository;

    @Override
    public void createPostRecode(Long userId, Long postId) {
        PostRecode postRecode = PostRecode.createOf(userId, postId);

        postRecodeRepository.save(postRecode);
    }

    @Override
    public Boolean isExistsReachedPostLimit(Long userId) {
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
            .withNano(0);
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)
            .withNano(999999999);

        Long userCreatePostInToday = postRecodeRepository.existsCreatePostInToday(userId,
            todayStart, todayEnd);

        return userCreatePostInToday <= 3;
    }
}
