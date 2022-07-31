package ca.dal.marketplace.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

// creating account verifications table in database
@Entity
@Table(name = "user_security_token")
@Getter
@Setter
public class UserSecurityToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "creation_date_time")
    @UpdateTimestamp
    private Date creationDateTime;

    // Joining table by connection of one to one for user to account_verification table with user_id
    // FetchType.EAGER is fetching the both tables data immediately for further usage
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserSecurityToken(){}

    public UserSecurityToken(User user){
        this.user = user;
        creationDateTime = new Date();
        token = UUID.randomUUID().toString();
    }
}
