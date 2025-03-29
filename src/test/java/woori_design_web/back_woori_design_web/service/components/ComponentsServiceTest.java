package woori_design_web.back_woori_design_web.service.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import woori_design_web.back_woori_design_web.dto.comment.CommentDto.CommentListResponse;
import woori_design_web.back_woori_design_web.entity.*;
import woori_design_web.back_woori_design_web.repository.CommentRepository;
import woori_design_web.back_woori_design_web.repository.ComponentsRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComponentsServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ComponentsRepository componentsRepository;

    @InjectMocks
    private ComponentsService componentsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCommentsByComponent() throws Exception {
        long componentId = 1L;
        Long userId = null;

        Member member = new Member(1L, "memberName", "password", SocialType.LOCAL, "email", Role.USER, UserStatus.ACTIVE, false, null, null, null, null);
        Components components = new Components(componentId, "componentName", "content", Authority.FREE, "/path");
        Comment comment1 = new Comment(1L, member, components, "content1");
        Comment comment2 = new Comment(2L, member, components, "content2");

        List<Comment> comments = Arrays.asList(comment1, comment2);

        when(commentRepository.findByComponentsId(componentId)).thenReturn(comments);

        CommentListResponse response = componentsService.getCommentsByComponent(componentId, userId);

        assertNotNull(response);
        assertEquals(2, response.comments().size());
        assertEquals(componentId, response.componentId());
        assertEquals("content1", response.comments().get(0).content());
        assertEquals("content2", response.comments().get(1).content());

        verify(commentRepository, times(1)).findByComponentsId(componentId);
    }
}