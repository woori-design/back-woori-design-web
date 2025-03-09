package woori_design_web.back_woori_design_web.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import woori_design_web.back_woori_design_web.entity.Member;
import woori_design_web.back_woori_design_web.entity.RefreshToken;
import woori_design_web.back_woori_design_web.repository.MemberRepository;
import woori_design_web.back_woori_design_web.repository.RefreshTokenRepository;
import woori_design_web.back_woori_design_web.security.dto.CustomMemberDetails;
import woori_design_web.back_woori_design_web.util.JwtUtil;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login";
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    /**
     * JWT 토큰 검증 및 인증 처리
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }

        // 사용자 요청 헤더에서 RefreshToken 추출
        // -> RefreshToken이 없거나 유효하지 않다면 (DB에 저장된 RefreshToken과 다르다면) null 반환
        // 사용자의 요청 헤더에 RefreshToken이 있는 경우는, AccessToken이 만료되어 요청한 경우만 존재
        // 위의 경우 제회하면 추출한 refreshToken은 모두 null
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // 리프레시 토큰이 요청 헤더에 존재했다면, 사용자가 AccessToken이 만료되어서
        // RefreshToken까지 보낸 것이므로 리프레시 토큰이 DB의 리프레시 토큰과 일치하는지 판단후
        // 일치시 AccessToken을 재발급해준다.
        if (refreshToken != null) {
            chechkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급하고 인증처리는 하지 않게 하기 위해 바로 return으로 필터 진행
            // 막기

        }

        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);

        }
    }

    /**
     * 필터 적용 제외 URL 설정
     * @param request
     * @return boolean
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/oauth2/authorization/kakao") || path.equals("/api/v1/refresh/token");
    }

    /**
     *  리프레시 토큰으로 유저 정보 찾기 & AccessToken / RefreshToken 재발급 메소드
     *  JWTService.createAccessToken() 으로 AccessToken 생성
     *  reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
     *  그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답헤더에 보내기
     */
    public void chechkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken ){

    }

    /**
     * refreshToken 재발급 & DB에 refreshToken 업데이트 메소드
     * jwtService.createRefreshToken()으로 RefreshToken 재발급 후
     * DB에 재발급한 리프레시 토큰 업데이트 후 Flush
     */
    private String reIssueRefreshToken(Member member){
        String reIssuedRefreshToken = jwtService.createRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findById(member.getId())
                .map(token -> {
                    token.update(reIssuedRefreshToken);
                    return token;
                })
                .orElseGet(()-> {
                    return RefreshToken.builder()
                            .id(member.getId())
                            .refreshToken(reIssuedRefreshToken)
                            .build();
                });

        refreshTokenRepository.save(refreshToken);
        return reIssuedRefreshToken;

    }

    /**
     * 엑세스 토큰 체크 및 인증 처리 메소드
     * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
     * 유효한 토큰이면, 액세스 토큰에서  extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
     * 그 유저 객체를 saveAuthentication으로 인증 처리
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     * 그 후, 다음 인증 필터로 진행
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(id -> memberRepository.findById(id)
                                .ifPresent(member -> saveAuthentication(member,accessToken))));

        filterChain.doFilter(request,response);
    }

    /**
     * 인증 허가 메소드
     * 파라미터의 유저 : 우리가 만든 회원 객체 | 빌더의 유저 : UserDetail 의 Member 객체
     *
     * new UsernamePasswordAuthenticationToken로 인증 객체인 Authentication 객체 생성
     * UsernamePasswordAuthenticationToken의 파라미터
     * 1. 위에서 만든 UserDetailUser 객체 (유저 정보)
     * 2. credential (보통 비밀번호로, 인증 시에는 보통 null로 제거)
     * 3. Collection < ? extends GrantedAuthority> authorities이 있어서 getter로 호출 후,
     * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체 생성하고, mapAuthorities()에 담기
     *
     * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
     * setAuthentication()을 이용하여 위에서 만든 Authication 객체에 대한 인증 허가 처리
     */
    public void saveAuthentication(Member member, String accessToken) {
        CustomMemberDetails customMemberDetails = CustomMemberDetails.builder()
                .id(member.getId())
                .email(member.getEmail())
                .accessToken(accessToken)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(member.getRole().toString())))
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(customMemberDetails, null,
                        authoritiesMapper.mapAuthorities(customMemberDetails.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}