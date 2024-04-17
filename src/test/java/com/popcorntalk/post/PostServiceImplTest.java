package com.popcorntalk.post;

import static com.popcorntalk.global.exception.ErrorCode.DELETE_POST_FOUND;
import static com.popcorntalk.global.exception.ErrorCode.PERMISSION_DENIED;
import static com.popcorntalk.global.exception.ErrorCode.POST_NOT_FOUND;
import static com.popcorntalk.global.exception.ErrorCode.SEARCH_POST_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.popcorntalk.domain.point.service.PointService;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.repository.PostRepository;
import com.popcorntalk.domain.post.service.PostRecodeService;
import com.popcorntalk.domain.post.service.PostServiceImpl;
import com.popcorntalk.domain.user.service.UserService;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.NotFoundException;
import com.popcorntalk.global.exception.customException.PermissionDeniedException;
import com.popcorntalk.mockData.MockData;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PostServiceImplTest extends MockData {

    @Mock
    PostRepository postRepository;
    @Mock
    PostRecodeService postRecodeService;
    @Mock
    PointService pointService;
    @Mock
    UserService userService;
    @InjectMocks
    PostServiceImpl postService;

    @Test
    @DisplayName("Post 단일조회 - 성공")
    void testGetPostByIdSuccess() {
        //given
        given(postRepository.findPost(any(Long.class))).willReturn(TEST_GET_RESPONSE_DTO);

        //when
        PostGetResponseDto response = postRepository.findPost(TEST_POST_ID);

        //then
        assertEquals(TEST_GET_RESPONSE_DTO.getName(), response.getName());
    }

    @Test
    @DisplayName("Post 단일조회 - 게시물없음")
    void testGetPostByIdNoPost() {
        //given
        given(postRepository.findPost(any(Long.class))).willThrow(
            new NotFoundException(SEARCH_POST_NOT_FOUND));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postRepository.findPost(TEST_POST_ID));

        //then
        assertEquals(SEARCH_POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 전체조회 - 성공")
    void testGetPostsSuccess() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        boolean hasNext = false;
        int type = 0;
        String keyword = "";


        List<PostGetResponseDto> TEST_GET_RESPONSE_DTO_LIST = new ArrayList<>();
        TEST_GET_RESPONSE_DTO_LIST.add(TEST_GET_RESPONSE_DTO);
        TEST_GET_RESPONSE_DTO_LIST.add(TEST_ANOTHER_GET_RESPONSE_DTO);
        Slice<PostGetResponseDto> responsesSlice = new SliceImpl<>(TEST_GET_RESPONSE_DTO_LIST,
            pageRequest, hasNext);

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willReturn(
            responsesSlice);

        //when
        Slice<PostGetResponseDto> returnResponses = postService.getNormalPosts(pageRequest, type,
            keyword);

        //then
        assertEquals(responsesSlice.get().count(), returnResponses.get().count());
        assertEquals(responsesSlice.getContent().get(0), returnResponses.getContent().get(0));
        assertEquals(responsesSlice.getContent().get(1), returnResponses.getContent().get(1));
    }

    @Test
    @DisplayName("Post 전체조회 - 게시물 없음")
    void testGetPostsNoPosts() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        int type = 0;
        String keyword = "";

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willThrow(
            new NotFoundException(SEARCH_POST_NOT_FOUND));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.getNormalPosts(pageRequest, type, keyword));

        //then
        assertEquals(SEARCH_POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 전체조회 - type:email 성공")
    void testGetPostsSearchTypeEmailSuccess() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        boolean hasNext = false;
        int type = 1;
        String keyword = EMAIL;

        List<PostGetResponseDto> TEST_GET_RESPONSE_DTO_LIST = new ArrayList<>();
        TEST_GET_RESPONSE_DTO_LIST.add(TEST_GET_RESPONSE_DTO);
        Slice<PostGetResponseDto> responsesSlice = new SliceImpl<>(TEST_GET_RESPONSE_DTO_LIST,
            pageRequest, hasNext);

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class)))
            .willReturn(responsesSlice);

        //when
        Slice<PostGetResponseDto> returnResponses = postService.getNormalPosts(pageRequest, type,
            keyword);

        //then
        assertEquals(responsesSlice.get().count(), returnResponses.get().count());
        assertEquals(keyword, returnResponses.get().findAny().get().getEmail());
    }

    @Test
    @DisplayName("Post 전체조회 - type:email 게시물 없음")
    void testGetPostsSearchTypeEmailNoPosts() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        int type = 1;
        String keyword = EMAIL;

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willThrow(
            new NotFoundException(SEARCH_POST_NOT_FOUND));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.getNormalPosts(pageRequest, type, keyword));

        //then
        assertEquals(SEARCH_POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 전체조회 - type:title 성공")
    void testGetPostsSearchTypeTitleSuccess() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        boolean hasNext = false;
        int type = 2;
        String keyword = POST_NAME;

        List<PostGetResponseDto> TEST_GET_RESPONSE_DTO_LIST = new ArrayList<>();
        TEST_GET_RESPONSE_DTO_LIST.add(TEST_GET_RESPONSE_DTO);
        TEST_GET_RESPONSE_DTO_LIST.add(TEST_ANOTHER_GET_RESPONSE_DTO);
        Slice<PostGetResponseDto> responsesSlice = new SliceImpl<>(TEST_GET_RESPONSE_DTO_LIST,
            pageRequest, hasNext);

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class)))
            .willReturn(responsesSlice);

        //when
        Slice<PostGetResponseDto> returnResponses = postService.getNormalPosts(pageRequest, type,
            keyword);

        //then
        assertEquals(responsesSlice.get().count(), returnResponses.get().count());
        assertTrue(returnResponses.get().anyMatch(i -> i.getName().contains(POST_NAME)));
    }

    @Test
    @DisplayName("Post 전체조회 - type:title 게시물 없음")
    void testGetPostsSearchTypeTitleNoPosts() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        int type = 2;
        String keyword = POST_NAME;

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willThrow(
            new NotFoundException(SEARCH_POST_NOT_FOUND));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.getNormalPosts(pageRequest, type, keyword));

        //then
        assertEquals(SEARCH_POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 전체조회 - type:title 검색어 없음")
    void testGetPostsSearchTypeTitleNoKeyword() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        int type = 2;
        String keyword = "";

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postService.getNormalPosts(pageRequest, type, keyword));

        //then
        assertEquals("검색어를 입력해 주세요", exception.getMessage());
    }

    @Test
    @DisplayName("Post 전체조회 - 존재하지 않는 타입")
    void testGetPostsSearchMissMatchType() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        int type = 3;
        String keyword = "";

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postService.getNormalPosts(pageRequest, type, keyword));

        //then
        assertEquals("검색조건이 맞지않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Notice Post 전체조회 - 성공")
    void testGetNoticePostsSuccess() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        boolean hasNext = false;

        List<PostGetResponseDto> TEST_NOTICE_GET_RESPONSE_DTO_LIST = new ArrayList<>();
        TEST_NOTICE_GET_RESPONSE_DTO_LIST.add(TEST_GET_NOTICE_RESPONSE_DTO);
        Slice<PostGetResponseDto> responsesSlice = new SliceImpl<>(
            TEST_NOTICE_GET_RESPONSE_DTO_LIST,
            pageRequest, hasNext);

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willReturn(
            responsesSlice);

        //when
        Slice<PostGetResponseDto> returnResponses = postService.getNoticePosts(pageRequest);

        //then
        assertEquals(responsesSlice.get().count(), returnResponses.get().count());
        assertEquals(responsesSlice.getContent().get(0), returnResponses.getContent().get(0));
    }

    @Test
    @DisplayName("Notice Post 전체조회 - 게시물 없음")
    void testGetNoticePostsNoPosts() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willThrow(
            new NotFoundException(SEARCH_POST_NOT_FOUND));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.getNoticePosts(pageRequest));

        //then
        assertEquals(SEARCH_POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("삭제된 Post 전체조회 - 성공")
    void testGetDeletePostsSuccess() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");
        boolean hasNext = false;

        List<PostGetResponseDto> TEST_DELETED_GET_RESPONSE_DTO_LIST = new ArrayList<>();
        TEST_DELETED_GET_RESPONSE_DTO_LIST.add(TEST_GET_DELETE_RESPONSE_DTO);
        Slice<PostGetResponseDto> responsesSlice = new SliceImpl<>(
            TEST_DELETED_GET_RESPONSE_DTO_LIST,
            pageRequest, hasNext);

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willReturn(
            responsesSlice);

        //when
        Slice<PostGetResponseDto> returnResponses = postService.getDeletePosts(TEST_ADMIN_USER,
            pageRequest);

        //then
        assertEquals(responsesSlice.get().count(), returnResponses.get().count());
        assertEquals(responsesSlice.getContent().get(0), returnResponses.getContent().get(0));
    }

    @Test
    @DisplayName("삭제된 Post 전체조회 - 게시물 없음")
    void testGetDeletePostsNoPosts() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAt");

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willThrow(
            new NotFoundException(SEARCH_POST_NOT_FOUND));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.getDeletePosts(TEST_ADMIN_USER, pageRequest));

        //then
        assertEquals(SEARCH_POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 생성 - 성공")
    void testCreatePostSuccess() {
        //given
        given(postRepository.save(any(Post.class))).willReturn(TEST_POST);

        //when
        postService.createPost(TEST_USER, TEST_CREATE_REQUEST_DTO);

        //then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("NoticePost 생성 - 성공")
    void testCreateNoticePostSuccess() {
        //given
        given(postRepository.save(any(Post.class))).willReturn(TEST_NOTICE_POST);

        //when
        postService.createNoticePost(TEST_ADMIN_USER, TEST_NOTICE_CREATE_REQUEST_DTO);

        //then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("Post 수정 - 성공")
    void testUpdatePostSuccess() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_POST));
        TEST_POST.update(TEST_UPDATE_REQUEST_DTO);

        //when
        postService.updatePost(TEST_USER, TEST_UPDATE_REQUEST_DTO, TEST_POST_ID);

        //then
        assertEquals(TEST_UPDATE_REQUEST_DTO.getPostName(), TEST_POST.getName());
        assertEquals(TEST_UPDATE_REQUEST_DTO.getPostContent(), TEST_POST.getContent());
        assertEquals(TEST_UPDATE_REQUEST_DTO.getPostImage(), TEST_POST.getImage());
    }

    @Test
    @DisplayName("Post 수정 - 게시물 없음")
    void testUpdatePostNoPost() {
        //given
        given(postRepository.findById(any(Long.class))).willThrow(
            new NotFoundException(SEARCH_POST_NOT_FOUND));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.updatePost(TEST_USER, TEST_UPDATE_REQUEST_DTO, TEST_POST_ID));

        //then
        assertEquals(SEARCH_POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 수정 - 삭제된 게시물")
    void testUpdatePostDeletePost() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(
            Optional.ofNullable(TEST_DELETE_POST));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.updatePost(TEST_USER, TEST_UPDATE_REQUEST_DTO, TEST_DELETE_POST_ID));

        //then
        assertEquals(DELETE_POST_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 수정 - 타인 게시물")
    void testUpdatePostNoPermission() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_POST));

        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
            postService.updatePost(TEST_ANTHER_USER, TEST_UPDATE_REQUEST_DTO, TEST_POST_ID));

        //then
        assertEquals(PERMISSION_DENIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 삭제 - 성공")
    void testDeletePostSuccess() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_POST));

        //when
        postService.deletePost(TEST_USER, TEST_POST_ID);

        //then
        assertEquals(DeletionStatus.Y, TEST_POST.getDeletionStatus());
    }

    @Test
    @DisplayName("Post 삭제 - 게시물 없음")
    void testDeletePostNoPost() {
        //given
        given(postRepository.findById(any(Long.class))).willThrow(
            new NotFoundException(POST_NOT_FOUND));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.deletePost(TEST_USER, TEST_POST_ID));

        //then
        assertEquals(POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 삭제 - 삭제된 게시물")
    void testDeletePostDeletePost() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(
            Optional.ofNullable(TEST_DELETE_POST));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.deletePost(TEST_USER, TEST_DELETE_POST_ID));

        //then
        assertEquals(DELETE_POST_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 삭제 - 타인 게시물")
    void testDeletePostNoPermission() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_POST));

        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
            postService.deletePost(TEST_ANTHER_USER, TEST_POST_ID));

        //then
        assertEquals(PERMISSION_DENIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 존재확인 - 성공")
    void testGetPostSuccess() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_POST));

        //when
        Post post = postService.getPost(any(Long.class));

        //then
        assertEquals(TEST_POST.getName(), post.getName());
    }

    @Test
    @DisplayName("Post 존재확인 - 게시물 없음")
    void testGetPostNoPost() {
        //given
        given(postRepository.findById(any(Long.class))).willThrow(
            new NotFoundException(POST_NOT_FOUND));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.getPost(any(Long.class)));

        //then
        assertEquals(POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 존재확인 - 삭제된 게시물")
    void testGetPostDeletePost() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(
            Optional.ofNullable(TEST_DELETE_POST));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            postService.getPost(any(Long.class)));

        //then
        assertEquals(DELETE_POST_FOUND, exception.getErrorCode());
    }
}
