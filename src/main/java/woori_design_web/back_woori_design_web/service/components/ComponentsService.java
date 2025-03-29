package woori_design_web.back_woori_design_web.service.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import woori_design_web.back_woori_design_web.dto.comment.CommentDto.CommentListResponse;
import woori_design_web.back_woori_design_web.dto.comment.CommentDto.CommentListResponse.CommentsResponse;
import woori_design_web.back_woori_design_web.entity.Comment;
import woori_design_web.back_woori_design_web.entity.Member;
import woori_design_web.back_woori_design_web.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComponentsService {
    private final CommentRepository commentRepository;

    /**
     * 컴포넌트 기반 댓글 목록 조회
     */
    public CommentListResponse getCommentsByComponent(long componentsId, Long userId) {
        List<Comment> comments = commentRepository.findByComponentsId(componentsId);
        List<CommentsResponse> commentsResponseList = comments.stream()
                .map(comment -> {
                    Member member = comment.getMember();
                    boolean isMine = member.getId().equals(userId);

                    return CommentsResponse.builder()
                            .id(comment.getId())
                            .user(CommentsResponse.UserInfo.builder()
                                    .id(member.getId())
                                    .nickname(member.getName())
                                    .build())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .isMine(isMine)
                            .build();
                })
                .collect(Collectors.toList());

        return CommentListResponse.builder()
                .componentId(componentsId)
                .comments(commentsResponseList)
                .build();
    }
}
