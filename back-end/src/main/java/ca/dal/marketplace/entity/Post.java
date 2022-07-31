package ca.dal.marketplace.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private String status;

    @Column(name = "closed_timestamp")
    private Timestamp closedTimestamp;

    @Column(name ="negotiable")
    private String negotiable;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "insert_timestamp")
    @UpdateTimestamp
    private Timestamp insertTimestamp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.EAGER)
    private Set<PostDetail> postDetails;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.EAGER)
    private Set<Media> media;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.EAGER)
    private Set<Buyer> buyers;

    public void add(PostDetail item) {

        if (item != null) {
            if (postDetails == null) {
                postDetails = new HashSet<>();
            }

            postDetails.add(item);
            item.setPost(this);
        }
    }

    public void add(Media item) {

        if (item != null) {
            if (media == null) {
                media = new HashSet<>();
            }

            media.add(item);
            item.setPost(this);
        }
    }
}