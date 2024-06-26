package com.inncretech.linkedin.Services;

import com.inncretech.linkedin.DTOs.CompanyDto;
import com.inncretech.linkedin.Mappers.CompanyMapper;
import com.inncretech.linkedin.Models.Company;
import com.inncretech.linkedin.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    public CompanyDto createCompany(CompanyDto companyDto)
    {
        Optional<Company> companyFromDB = companyRepository.findByName(companyDto.getName().toLowerCase());
        if(companyFromDB.isPresent() && !companyFromDB.get().isDeleted())
        {
            throw new IllegalArgumentException("Company with " + companyDto.getName() + " already exists!");
        }

        Company company = companyMapper.companyDtoToCompany(companyDto);
        company = companyRepository.save(company);
        return companyMapper.companyToCompanyDto(company);
    }

    public CompanyDto getCompanyById(Long id)
    {
        Optional<Company> companyFromDB = companyRepository.findById(id);
        if(companyFromDB.isPresent() && !companyFromDB.get().isDeleted())
        {
            Company company = companyFromDB.get();
            return companyMapper.companyToCompanyDto(company);
        }
        else {
            throw new IllegalArgumentException("Company Not found");
        }
    }

    public List<CompanyDto> getAllCompanies()
    {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .filter(company -> !company.isDeleted())
                .map(companyMapper::companyToCompanyDto)
                .collect(Collectors.toList());
    }

    public CompanyDto updateCompany(Long id, CompanyDto companyDto)
    {
        Optional<Company> company = companyRepository.findById(id);
        if(company.isPresent() && !company.get().isDeleted())
        {
            Company updateCompany = company.get();
            updateCompany.setName(companyDto.getName());
            updateCompany.setProfile(companyDto.getProfile());
            updateCompany.setUpdatedAt(LocalDateTime.now());

            companyRepository.save(updateCompany);
            return companyMapper.companyToCompanyDto(updateCompany);
        }
        else{
            throw new IllegalArgumentException("Company Not Found");
        }
    }

    public void DeleteCompany(Long id)
    {
        Optional<Company> company = companyRepository.findById(id);
        if(company.isPresent() && !company.get().isDeleted())
        {
            Company deleteCompany = company.get();
            deleteCompany.setUpdatedAt(LocalDateTime.now());
            deleteCompany.setDeleted(true);
            companyRepository.save(deleteCompany);

        }
        else{
            throw new IllegalArgumentException("Company Not Found");
        }
    }
}
