package ca.dal.marketplace.dto;

import ca.dal.marketplace.entity.User;
import ca.dal.marketplace.service.impl.UserRegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

class UniversityDomainDtoTest {

    @InjectMocks
    private String domain;

    @InjectMocks
    UniversityDomainDto universityDomainDto= new UniversityDomainDto(domain);

    @Test
    void getDomain() {
        String  expectedDomain = "dal";
        universityDomainDto.setDomain("dal");
        String domain = universityDomainDto.getDomain();
        assertEquals(expectedDomain,domain);
    }

    @Test
    void setDomain() {
        String  domain = "dal";
        universityDomainDto.setDomain(domain);
        assertEquals(universityDomainDto.getDomain(), domain);
    }
}