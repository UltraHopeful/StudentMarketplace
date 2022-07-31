package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.dao.PostDetailRepository;
import ca.dal.marketplace.entity.PostDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PostDetailServiceImplTest {

    @Mock
    private PostDetailRepository postDetailRepository;

    @InjectMocks
    private PostDetail postDetail;

    @InjectMocks
    private PostDetailServiceImpl postDetailService = new PostDetailServiceImpl();

    @BeforeEach
    void setUp(){
        postDetail.setFieldName("Coursebook");
        postDetail.setFieldValue("Yes");
    }
    @Test
    void findPostDetailByFieldNameInAndFieldValueIn() {
        String fieldName = "Condition";
        String fieldValue = "Excellent";
        Set<PostDetail> postDetails = postDetailService.findPostDetailByFieldNameAndFieldValue(fieldName,fieldValue);
        assertEquals(postDetailRepository.findPostDetailByFieldNameAndFieldValue(fieldName,fieldValue),postDetails);

    }

}