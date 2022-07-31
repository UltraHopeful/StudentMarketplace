package ca.dal.marketplace.dao;

import ca.dal.marketplace.dto.UniversityDomainDto;
import ca.dal.marketplace.entity.UniversityDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins="*")
public interface UniversityDomainRepository extends JpaRepository<UniversityDomain, Long> {
    @Query(name="find_university_domain",nativeQuery = true)
    UniversityDomainDto findByUniversityDomain(@Param("domains") String domains);
}