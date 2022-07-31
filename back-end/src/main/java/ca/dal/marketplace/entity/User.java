package ca.dal.marketplace.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false)
    @Email
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Column(name = "failed_attempts")
    private Integer failedAttempts;

    @Column(name = "is_locked")
    private boolean isLocked;

    @Column(name = "is_verified")
    private boolean isEnabled;

    @Column(name = "insert_timestamp")
    @UpdateTimestamp
    private Date insertTimestamp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seller")
    private Set<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserSecurityToken> userSecurityTokenList;

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("User{");
        builder.append("id=").append(id);
        builder.append(", firstName='").append(firstName).append("'");
        builder.append(", lastName='").append(lastName).append("'");
        builder.append(", email='").append(email).append("'");
        builder.append(", password='").append(password).append("'");
        builder.append(", phoneNumber='").append(phoneNumber).append("'");
        builder.append(", dateOfBirth='").append(dateOfBirth).append("'");
        builder.append(", failedAttempts='").append(failedAttempts).append("'");
        builder.append(", isLocked='").append(isLocked).append("'");
        builder.append(", isEnabled='").append(isEnabled).append("'");
        builder.append(", insertTimestamp='").append(insertTimestamp).append("'");
        builder.append(", posts='").append(posts);
        builder.append("}");

        return builder.toString();
    }
}
