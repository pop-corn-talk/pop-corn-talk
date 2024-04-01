package com.popcorntalk.domain.post.service;

import com.popcorntalk.domain.post.entity.PostRecode;
import com.popcorntalk.domain.post.repository.PostRecodeRepository;
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
}
