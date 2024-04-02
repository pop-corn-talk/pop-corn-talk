package com.popcorntalk.domain.post.service;

import com.popcorntalk.domain.post.entity.PostRecode;
import com.popcorntalk.domain.post.repository.PostRecodeRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostRecodeServiceImpl implements PostRecodeService {

    private final PostRecodeRepository postRecodeRepository;

    @Override
    @Transactional
    public void createPostRecode(Long userId, Long postId) {
        PostRecode postRecode = PostRecode.createOf(userId, postId);

        postRecodeRepository.save(postRecode);
    }

    @Override
    public int getPostCountInToday(Long userId) {
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
            .withNano(0);
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)
            .withNano(999999999);

        return postRecodeRepository.getCreatePostCountInToday(userId, todayStart, todayEnd);
    }

    @Override
    @Scheduled(cron = "1 0 0 * * *")
    @Transactional
    public void deletePostRecode() {
        LocalDateTime beforeSevenDays = LocalDateTime.now().minusDays(7);
        List<PostRecode> deletePostRecodes = postRecodeRepository.findOlderThanSevenDaysPosts(
            beforeSevenDays);
        if (!deletePostRecodes.isEmpty()) {
            postRecodeRepository.deleteAllInBatch(deletePostRecodes);
        }
    }
}
