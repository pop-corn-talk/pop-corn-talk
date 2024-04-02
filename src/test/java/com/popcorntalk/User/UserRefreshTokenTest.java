package com.popcorntalk.User;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import com.popcorntalk.global.entity.RefreshToken;
import com.popcorntalk.global.util.JwtUtil;
import com.popcorntalk.global.util.RedisUtil;
import java.security.Key;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.RedisTemplate;

@DisplayName("JWT Redis 테스트")
@DataRedisTest
public class UserRefreshTokenTest {
  String email = "email99@email.com";
  Long userId = 99L;

  @Test
  @DisplayName("토큰 생성 테스트")
  void createToken(){
    //given
    Key key;
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    RedisUtil redisUtil = new RedisUtil(redisTemplate);
    JwtUtil jwtUtil = new JwtUtil(redisUtil);

    String redisKeys = "ID : " + userId;

    //when
    when(jwtUtil.createToken(userId,email)).thenReturn("token");

    String token = jwtUtil.createToken(userId,email);
    RefreshToken refreshToken = (RefreshToken) redisUtil.get(redisKeys);

    //then

    assertEquals("리프레시 토큰",refreshToken.getPreviousAccessToken(),token);
  }


}
