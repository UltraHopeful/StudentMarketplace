package ca.dal.marketplace.entity;

import ca.dal.marketplace.dto.UniversityDomainDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NamedNativeQuery
        (name="find_university_domain",
            query = "SELECT d.domains as U_domains FROM university_domains d WHERE domains = :domains",
        resultSetMapping = "university_domain_dto")
@SqlResultSetMapping
        (name="university_domain_dto",
        classes = @ConstructorResult(targetClass = UniversityDomainDto.class,columns = {@ColumnResult(name="U_domains",type = String.class)}))
public class UniversityDomain {
    @Id
    @Column(name = "university_id", nullable = false)
    private Integer university_id;

    private String domains;

    public Integer getUniversity_id() {
        return university_id;
    }

    public void setUniversity_id(Integer university_id) {
        this.university_id = university_id;
    }

    public String getDomains() {
        return domains;
    }

    public void setDomains(String domains) {
        this.domains = domains;
    }
}
