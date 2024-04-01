package com.popcorntalk.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.global.exception.customException.LogoutException;
import com.popcorntalk.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
    String tokenValue = jwtUtil.getJwtFromHeader(req);

    if (StringUtils.hasText(tokenValue)) { // 액세스
      try {
        Date date = new Date();
        if (jwtUtil.getMemberInfoFromExpiredToken(tokenValue).getExpiration().compareTo(date) < 0) {

          String token = jwtUtil.validateRefreshToken(
              jwtUtil.getMemberInfoFromExpiredToken(tokenValue)
                  .get("userId", Long.class)
              , tokenValue);

          res.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
          ObjectNode json = new ObjectMapper().createObjectNode();
          json.put("message", "새로운 토큰이 발급되었습니다.");
          String newResponse = new ObjectMapper().writeValueAsString(json);
          res.setContentType("application/json");
          res.setContentLength(newResponse.length());
          res.getOutputStream().write(newResponse.getBytes());
          return;
        }
        if (!jwtUtil.validateToken(tokenValue)) {
          throw new LogoutException("로그아웃 상태입니다.");
        }
        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
        setAuthentication(info);
      } catch (SecurityException | MalformedJwtException | SignatureException e) {
        log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        return;
      } catch (ExpiredJwtException e) {
        log.error("Expired Refresh JWT token, 만료된 Refresh JWT token 입니다.");
        //예외 안던지고 준다 굳이 던져줄 필요 없다. 사용성이 안좋아진다. 던져주면 프론트 해줘
        return;
      } catch (UnsupportedJwtException e) {
        log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        return;
      } catch (IllegalArgumentException e) {
        log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        return;
      } catch (Exception e) {
        log.error(e.getMessage());
        return;
      }
    }
    filterChain.doFilter(req, res);
  }

  // 인증 처리
  public void setAuthentication(Claims info) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = createAuthentication(info);
    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);
  }

  // 인증 객체 생성
  private Authentication createAuthentication(Claims info) {
    User user = new User(info.get("userId", Long.class), info.get("email", String.class));
    UserDetails userDetails = new UserDetailsImpl(user);
    return new CustomAuthentication(userDetails);
  }
}
