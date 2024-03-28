package com.popcorntalk.global.util;

import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.global.dto.RefreshToken;
import com.popcorntalk.global.repository.RefreshTokenRepository;
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
import java.util.Optional;
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

    private final RefreshTokenRepository refreshTokenRepository;

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자 꼭 붙일 필요는 없지만 규칙
    public static final String BEARER_PREFIX = "Bearer ";
    //토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L;

    private final long REFRESHTOKENTIME = 60 * 60 * 1000 * 24 * 7L;

    @Value("${jwt.secret.key}") //application.properties에 들어있던 값이 들어감
    private String secretKey;
    //secretKey를 넣을 Key 객체
    private Key key;
    //알고리즘 선택
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct //딱 한번만 받아 오면 되는 값을 받을 때 요청을 새로 받는 걸 방지
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); //base64로 디코딩
        key = Keys.hmacShaKeyFor(bytes); //key에다가 디코딩한 secretKey를 넣습니다.
    }

    // 토큰 생성
    public String createToken(Long userId, String email) {
        Date date = new Date();

        String accessToken = recreationAccessToken(userId, email);

        deleteRefreshToken(userId);
        String refreshToken = BEARER_PREFIX +
            Jwts.builder()
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", UserRoleEnum.USER.toString())
                .setIssuedAt(new Date(date.getTime())) // 토큰 발행 시간 정보
                .setExpiration(new Date(date.getTime() + REFRESHTOKENTIME)) // set Expire Time
                .signWith(key, signatureAlgorithm)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
        RefreshToken token = RefreshToken.builder().refreshToken(refreshToken).userId(userId)
            .build();
        refreshTokenRepository.save(token);

        return accessToken;
    }

    public void deleteRefreshToken(Long userId) {
        Optional<RefreshToken> checkToken = refreshTokenRepository.findByUserId(userId);
        checkToken.ifPresent(refreshTokenRepository::delete);
        //만약 로그아웃이 없다면 업데이트가 맞다 로그아웃은 사실 프론트꺼다
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
        //validateToken에서 검증을 한 토큰의 body를 가져옴 claims라는 데이터의 집합으로 반환함
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            if (redisUtil.hasKeyBlackList(token)) {
                return false;
            }
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("토큰검증오류");
            return false;
        }
    }

    public String validateRefreshToken(Long userId) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId).orElseThrow(
            () -> new IllegalArgumentException("RefreshToken 이 유효하지 않습니다.")
        );
        String refreshToken = token.getRefreshToken().substring(7);
        Claims info = Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(refreshToken).getBody();
        return recreationAccessToken(info.get("userId", Long.class),
            info.get("email", String.class));
    }

    public String recreationAccessToken(Long userId, String email) {
        Date date = new Date();
        String accessToken = BEARER_PREFIX + // bearer 을 앞에 붙어줌
            Jwts.builder()
                .claim("userId", userId)
                .claim("email", email)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();

        return accessToken;
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(accessToken)
            .getBody()
            .getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public Claims getMemberInfoFromExpiredToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
