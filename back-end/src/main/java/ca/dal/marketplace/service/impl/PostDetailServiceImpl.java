package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.dao.PostDetailRepository;
import ca.dal.marketplace.entity.PostDetail;
import ca.dal.marketplace.service.PostDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostDetailServiceImpl implements PostDetailService {
    @Autowired
    private PostDetailRepository postDetailRepository;

    @Override
    public Set<PostDetail> findPostDetailByFieldNameAndFieldValue(String fieldName, String fieldValue) {
        return postDetailRepository.findPostDetailByFieldNameAndFieldValue(fieldName, fieldValue);
    }
}
