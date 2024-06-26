package com.inncretech.linkedin.Controller;

import com.inncretech.linkedin.DTOs.CompanyFollowersDto;
import com.inncretech.linkedin.Models.Company_followers;
import com.inncretech.linkedin.Services.CompanyFollowerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companyFollowers")
public class CompanyFollowersController {

    @Autowired
    private CompanyFollowerService companyFollowersService;

    @GetMapping("/getFollowersByCompany/{id}")
    public ResponseEntity<List<Company_followers>> getFollowersByCompany(@PathVariable Long id) {
        List<Company_followers> followers = companyFollowersService.getAllCompanyFollowers(id);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @PostMapping("/createFollowerRequest")
    public ResponseEntity<String> createCompanyFollower(@Valid @RequestBody CompanyFollowersDto companyFollowersDto) {
        companyFollowersService.createCompanyFollower(companyFollowersDto);
        return new ResponseEntity<>("Follower updated", HttpStatus.CREATED);
    }
}
