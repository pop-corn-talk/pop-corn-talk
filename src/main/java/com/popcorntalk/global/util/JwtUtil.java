package com.popcorntalk.global.util;

import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.global.entity.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

  private final RedisUtil redisUtil;


  // Header KEY 값
  public static final String AUTHORIZATION_HEADER = "Authorization";
  // 사용자 권한 값의 KEY
  public static final String BEARER_PREFIX = "Bearer ";
  //토큰 만료시간
  private final long TOKEN_TIME = 60 * 60 * 1000L;

  private final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 1000L;

  private String redisKeys;

  @Value("${jwt.secret.key}")
  private String secretKey;
  //secretKey 를 넣을 Key 객체
  private Key key;
  //알고리즘 선택
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct //딱 한번만 받아 오면 되는 값을 받을 때 요청을 새로 받는 걸 방지
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey); //base64로 디코딩
    key = Keys.hmacShaKeyFor(bytes); //key에다가 디코딩한 secretKey를 넣습니다.
  }

  // 로그인시 토큰 생성
  public String createToken(Long userId, String email) {
    Date date = new Date();
    String accessToken = createAccessToken(userId, email);

    try {
      UpdateValidRefreshToken(accessToken, userId);
    } catch (Exception e) {
      log.error("새로운 refresh 토큰 생성");
      SaveNewRefreshToken(date, accessToken, userId, email);
    }

    return accessToken;
  }

  private void UpdateValidRefreshToken(String accessToken, Long userId) {

    redisKeys = "ID : " + userId;

    RefreshToken refreshToken = (RefreshToken) redisUtil.get(redisKeys);
    refreshToken.update(accessToken);

    redisUtil.set(redisKeys, refreshToken, (int) REFRESH_TOKEN_TIME);
    log.error("기존 refresh 토큰 으로 생성");
  }

  private void SaveNewRefreshToken(Date date, String accessToken, Long userId, String email) {

    String refreshToken = BEARER_PREFIX +
        Jwts.builder()
            .claim("userId", userId)
            .claim("email", email)
            .claim("role", UserRoleEnum.USER.toString())
            .setIssuedAt(new Date(date.getTime())) // 토큰 발행 시간 정보
            .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // set Expire Time
            .signWith(key, signatureAlgorithm)  // 사용할 암호화 알고리즘과
            .compact();// signature 에 들어갈 secret 값 세팅

    RefreshToken token = RefreshToken
        .builder()
        .refreshToken(refreshToken)
        .previousAccessToken(accessToken.substring(7))
        .userId(userId)
        .build();

    redisKeys = "ID : " + userId;
    redisUtil.set(redisKeys, token, (int) REFRESH_TOKEN_TIME);
  }

  // header 에서 JWT 가져오기
  public String getJwtFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      //순수한 토큰이 필요하기 때문에 bearer 을 잘라버림
      return bearerToken.substring(7);
    }
    return null;
  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.error("토큰검증오류");
      return false;
    }
  }

  public String validateRefreshToken(Long userId, String previousJwt) {

    redisKeys = "ID : " + userId;
    RefreshToken refreshToken = new RefreshToken();
    if (redisUtil.hasKey(redisKeys)) {
      refreshToken = (RefreshToken) redisUtil.get(redisKeys);
    }

    if (!refreshToken.getPreviousAccessToken().equals(previousJwt)) {
      System.out.println(refreshToken.getPreviousAccessToken());
      redisUtil.delete(redisKeys);
      throw new NoSuchElementException("JWT refresh 인증 문제!!! 강제 로그아웃 합니다");
    }

    Claims info = Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(refreshToken.getRefreshToken().substring(7)).getBody();

    String token = createAccessToken(refreshToken.getUserId(),
        info.get("email", String.class));
    refreshToken.update(token);
    redisUtil.set(redisKeys, refreshToken, (int) REFRESH_TOKEN_TIME);
    return token;
  }

  public String createAccessToken(Long userId, String email) {
    Date date = new Date();
    return BEARER_PREFIX +
        Jwts.builder()
            .claim("userId", userId)
            .claim("email", email)
            .setExpiration(new Date(date.getTime() + TOKEN_TIME))
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  // TODO 이미 만들어 저 있던 FUNCTION 입니다 일단 팀원들의 의견을 듣고 지우/유지 하겠습니다. !
//  public Long getExpiration(String accessToken) {
//    Date expiration = Jwts.parserBuilder()
//        .setSigningKey(key)
//        .build()
//        .parseClaimsJws(accessToken)
//        .getBody()
//        .getExpiration();
//    Long now = new Date().getTime();
//    return (expiration.getTime() - now);
//  }

  public Claims getMemberInfoFromExpiredToken(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
