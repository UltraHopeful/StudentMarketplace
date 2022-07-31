package ca.dal.marketplace.dto;

import lombok.Data;



public class UniversityDomainDto {
    private String domain;

    public UniversityDomainDto(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
