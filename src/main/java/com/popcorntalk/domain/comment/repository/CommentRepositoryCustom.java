package com.popcorntalk.domain.comment.repository;

import com.popcorntalk.domain.comment.dto.CommentGetResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<CommentGetResponseDto> findComments(Long postId, Pageable pageable);
    List<Long> getDailyTop3PostsUserIds();
}
