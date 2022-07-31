package ca.dal.marketplace.service;

import ca.dal.marketplace.entity.PostDetail;

import java.util.Set;

public interface PostDetailService {

    Set<PostDetail> findPostDetailByFieldNameAndFieldValue(String fieldName, String fieldValue);
}
