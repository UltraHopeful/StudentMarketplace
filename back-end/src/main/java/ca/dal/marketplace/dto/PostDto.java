package ca.dal.marketplace.dto;

import ca.dal.marketplace.entity.Media;
import ca.dal.marketplace.entity.Post;
import ca.dal.marketplace.entity.PostDetail;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Transient;

import java.util.Set;

@Data
@Transient
public class PostDto {
    Post post;
    Set<PostDetail> postDetails;
    Set<Media> media;
}