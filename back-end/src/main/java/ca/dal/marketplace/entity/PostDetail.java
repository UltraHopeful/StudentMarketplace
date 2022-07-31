package ca.dal.marketplace.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post_details")
@Getter
@Setter
public class PostDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_detail_id")
    private Long postDetailId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "field_value")
    private String fieldValue;

    @Column(name = "insert_timestamp")
    @UpdateTimestamp
    private Timestamp insertTimestamp;
}
