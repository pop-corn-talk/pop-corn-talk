package com.popcorntalk.global.util;

import com.popcorntalk.domain.user.entity.UserRoleEnum;

import com.popcorntalk.global.entity.RefreshToken;
import com.popcorntalk.global.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.NoSuchElementException;
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
    private final long TOKEN_TIME =  60 * 1000L;

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

//        deleteRefreshToken(userId);

        String accessToken = recreationAccessToken(userId, email);

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
        RefreshToken token = RefreshToken
            .builder()
            .refreshToken(refreshToken)
            .previousAccessToken(accessToken.substring(7))
            .userId(userId)
            .build();
        refreshTokenRepository.save(token);

        return accessToken;
    }

    public void deleteRefreshToken(Long userId) {
        Optional<RefreshToken> checkToken = refreshTokenRepository.findByUserId(userId);
        checkToken.ifPresent(refreshTokenRepository::delete);
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
    @Transactional
    public String validateRefreshToken(Long userId,String previousJwt) {

        RefreshToken token = refreshTokenRepository.findByUserIdAndAndPreviousAccessToken(userId,previousJwt).orElseThrow(
            () -> new IllegalArgumentException("최신 발급한 AccessToken 이 아니거나 RefreshToken 이 유효하지 않습니다.")
        );
        String refreshToken = token.getRefreshToken().substring(7);
        Claims info = Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(refreshToken).getBody();
        return recreationAccessToken(info.get("userId", Long.class),
            info.get("email", String.class));
    }

    public String recreationAccessToken(Long userId, String email) {
        Date date = new Date();
        String accessToken = BEARER_PREFIX +
            Jwts.builder()
                .claim("userId", userId)
                .claim("email", email)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
        try{
            RefreshToken token = refreshTokenRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("RefreshToken 이 유효하지 않습니다.")
            );
            token.update(accessToken);
        }catch (Exception e){
            log.error("현제 refresh 토큰이 없습니다, refresh 토큰을 발급 합니다.");
        }
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
