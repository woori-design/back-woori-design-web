package woori_design_web.back_woori_design_web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import woori_design_web.back_woori_design_web.entity.Role;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.access-token-secret}")
    private String ACCESS_TOKEN_SECRET;

    @Value("${jwt.refresh-token-secret}")
    private String REFRESH_TOKEN_SECRET;

    private final long DEFAULT_ACCESS_TOKEN_EXPIRY = 15 * 60 * 1000L; // 15분
    private final long DEFAULT_REFRESH_TOKEN_EXPIRY = 14 * 24 * 60 * 60 * 1000L; // 14일

    /**
     * 액세스 토큰 생성
     * @param memberId
     * @param email
     * @param role
     * @return 액세스 토큰
     */
    public String generateToken(Long memberId, String email, Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("memberId", memberId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_ACCESS_TOKEN_EXPIRY))
                .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET)
                .compact();
    }

    /**
     * 액세스 토큰 생성(만료 시간 설정)
     * @param memberId
     * @param email
     * @param role
     * @param accessTokenExpiresIn
     * @return 액세스 토큰
     */
    public String generateToken(Long memberId, String email, Role role, Integer accessTokenExpiresIn) {
        long expiry = (accessTokenExpiresIn != null) ? accessTokenExpiresIn * 1000L : DEFAULT_ACCESS_TOKEN_EXPIRY;
        return Jwts.builder()
                .setSubject(email)
                .claim("memberId", memberId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET)
                .compact();
    }

    /**
     * 리프레시 토큰 생성
     * @param memberId
     * @return 리프레시 토큰
     */
    public String generateRefreshToken(Long memberId) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_REFRESH_TOKEN_EXPIRY))
                .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_SECRET)
                .compact();
    }

    /**
     * 리프레시 토큰 생성(만료 시간 설정)
     * @param memberId
     * @param refreshTokenExpiresIn
     * @return 리프레시 토큰
     */
    public String generateRefreshToken(Long memberId, Integer refreshTokenExpiresIn) {
        long expiry = (refreshTokenExpiresIn != null) ? refreshTokenExpiresIn * 1000L : DEFAULT_REFRESH_TOKEN_EXPIRY;
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_SECRET)
                .compact();
    }

    /**
     * 액세스 토큰 유효성 검사
     * @param token
     * @return token claims
     */
    public Claims validateAccessToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(ACCESS_TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid or expired access token: " + e.getMessage());
        }
    }

    /**
     * 리프레시 토큰 유효성 검사
     * @param refreshToken 리프레시 토큰
     * @return token claims
     */
    public Claims validateRefreshToken(String refreshToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(REFRESH_TOKEN_SECRET)
                    .parseClaimsJws(refreshToken)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid or expired refresh token: " + e.getMessage());
        }
    }
}