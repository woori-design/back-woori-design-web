package woori_design_web.back_woori_design_web.dto.comment;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record CommentDto() {
    /**
     * 컴포넌트 댓글 조회 응답 Dto
     * @param componentId
     * @param comments
     */
    @Builder
    public record CommentListResponse(
            long componentId,
            List<CommentsResponse> comments
    ) {
        @Builder
        public record CommentsResponse(
                long id,
                UserInfo user,
                String content,
                LocalDateTime createdAt,
                boolean isMine // 사용자 본인이 작성한 댓글 판단 여부
        ) {
            @Builder
            public record UserInfo(
                    long id,
                    String nickname
            ) {}
        }
    }
}
