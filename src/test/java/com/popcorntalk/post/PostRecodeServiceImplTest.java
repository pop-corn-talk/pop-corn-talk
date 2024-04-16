package com.popcorntalk.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.popcorntalk.domain.post.entity.PostRecode;
import com.popcorntalk.domain.post.repository.PostRecodeRepository;
import com.popcorntalk.domain.post.service.PostRecodeServiceImpl;
import com.popcorntalk.mockData.MockData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PostRecodeServiceImplTest extends MockData {

    @Mock
    PostRecodeRepository postRecodeRepository;
    @InjectMocks
    PostRecodeServiceImpl postRecodeService;

    @Test
    @DisplayName("PostRecode 등록 - 성공")
    void testCreatePostRecodeSuccess() {
        //given
        given(postRecodeRepository.save(any(PostRecode.class))).willReturn(TEST_POSTRECODE);

        //when
        postRecodeService.createPostRecode(TEST_USER_ID, TEST_POST_ID);

        //then
        verify(postRecodeRepository, times(1)).save(any(PostRecode.class));
    }

    @Test
    @DisplayName("당일 작성한 PostRecode 조회 - 성공")
    void testGetPostCountInTodaySuccess() {
        //given
        int count = 3;
        given(postRecodeRepository.getCreatePostCountInToday(any(Long.class),
            any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(count);

        //when
        postRecodeService.getPostCountInToday(TEST_USER_ID);

        //then
        verify(postRecodeRepository, times(1)).getCreatePostCountInToday(any(Long.class),
            any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("일주일 지난 PostRecode 삭제 - 성공")
    void testDeletePostRecodeSuccess() {
        //given
        List<PostRecode> postRecodes = new ArrayList<>();
        postRecodes.add(TEST_POSTRECODE);
        given(
            postRecodeRepository.findOlderThanSevenDaysPosts(any(LocalDateTime.class))).willReturn(
            postRecodes);
        //when
        postRecodeService.deletePostRecode();

        //then
        verify(postRecodeRepository, times(1)).deleteAllInBatch(anyList());
    }

    @Test
    @DisplayName("일주일 지난 PostRecode 삭제 - 없을경우")
    void testDeletePostRecodeNoPostRecode() {
        //given
        //when
        postRecodeService.deletePostRecode();

        //then
        verify(postRecodeRepository, never()).deleteAllInBatch(anyList());
    }
}
