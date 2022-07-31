package ca.dal.marketplace.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "media")
@Getter
@Setter
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long mediaId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "location")
    private String location;

    @Column(name = "insert_timestamp")
    @UpdateTimestamp
    private Timestamp insertTimestamp;
}
