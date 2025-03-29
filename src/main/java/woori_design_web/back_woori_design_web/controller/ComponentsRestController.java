package woori_design_web.back_woori_design_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woori_design_web.back_woori_design_web.dto.comment.CommentDto.CommentListResponse;
import woori_design_web.back_woori_design_web.dto.common.ResponseDto;
import woori_design_web.back_woori_design_web.service.components.ComponentsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/components")
public class ComponentsRestController {
    private final ComponentsService componentsService;

    /**
     * 컴포넌트 기반 댓글 목록 조회
     * @param componentId 컴포넌트 ID
     * @return CommentListResponse (componentId, comments) 컴포넌트 댓글 목록 응답
     */
    @GetMapping("/{componentId}/comments")
    public ResponseEntity<ResponseDto<CommentListResponse>> getCommentsByComponent(@PathVariable long componentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 추후 사용자 ID 가져오는 로직 추가
//            Long userId = (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails)
//                    ? ((CustomUserDetails) authentication.getPrincipal()).getId()
//                    : null;
            Long userId = null;
            CommentListResponse response = componentsService.getCommentsByComponent(componentId, userId);
            return ResponseEntity.ok(new ResponseDto<>(ResponseDto.Status.SUCCESS, "댓글 목록 조회 성공", response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(ResponseDto.Status.FAILURE, "댓글 목록 조회 실패", null));
        }
    }
}
