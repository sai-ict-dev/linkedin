package com.inncretech.linkedin.Mappers;

import com.inncretech.linkedin.DTOs.CompanyDto;
import com.inncretech.linkedin.Models.Company;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CompanyMapper {

    public CompanyDto companyToCompanyDto(Company company)
    {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName(company.getName());
        companyDto.setProfile(company.getProfile());

        return companyDto;
    }

    public Company companyDtoToCompany(CompanyDto companyDto)
    {
        Company company = new Company();
        company.setName(companyDto.getName().toLowerCase());
        company.setProfile(companyDto.getProfile());
        company.setDeleted(false);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());
        return company;
    }
}
