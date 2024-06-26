package com.inncretech.linkedin.Services;

import com.inncretech.linkedin.DTOs.CompanyFollowersDto;
import com.inncretech.linkedin.Models.Company;
import com.inncretech.linkedin.Models.Company_followers;
import com.inncretech.linkedin.Models.Roles;
import com.inncretech.linkedin.Models.User;
import com.inncretech.linkedin.Repository.CompanyFollowerRepository;
import com.inncretech.linkedin.Repository.CompanyRepository;
import com.inncretech.linkedin.Repository.RoleRepository;
import com.inncretech.linkedin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyFollowerService {

    @Autowired
    private CompanyFollowerRepository companyFollowerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<Company_followers> getAllCompanyFollowers(Long companyId) {
        return companyFollowerRepository.findByCompanyId(companyId);
    }

    public void createCompanyFollower(CompanyFollowersDto companyFollowersDto) {
        Optional<Company> company = companyRepository.findById(companyFollowersDto.getCompanyId());
        if (!company.isPresent() || company.get().isDeleted()) {
            throw new IllegalArgumentException("Company Not Found with the given Id");
        }


        Optional<User> user = userRepository.findById(companyFollowersDto.getFollowerId());
        if (!user.isPresent() || user.get().isDeleted()) {
        throw new IllegalArgumentException("Follower Not Found with the given Id");
         }

        Optional<Roles> roles = roleRepository.findById(companyFollowersDto.getRoleId());
        if(!roles.isPresent() || roles.get().isDeleted()) {

            throw new IllegalArgumentException("role Not Found with the given Id");
        }

        Company_followers companyFollowers = new Company_followers();
        companyFollowers.setCompany(company.get());
        companyFollowers.setUser(user.get());
        companyFollowers.setRole(roles.get());
        companyFollowerRepository.save(companyFollowers);
    }

}
