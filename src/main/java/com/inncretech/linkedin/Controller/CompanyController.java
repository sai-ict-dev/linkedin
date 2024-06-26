package com.inncretech.linkedin.Controller;

import com.inncretech.linkedin.DTOs.CompanyDto;
import com.inncretech.linkedin.Services.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/Company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id)
    {
        CompanyDto company = companyService.getCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<CompanyDto>> getAllCompanies()
    {
        List<CompanyDto> companies = companyService.getAllCompanies();
        return new ResponseEntity<>(companies,HttpStatus.OK);
    }

    @PostMapping("/createCompany")
    public ResponseEntity<CompanyDto> createCompany(@Valid @RequestBody CompanyDto companyDto)
    {
        CompanyDto createdCompany = companyService.createCompany(companyDto);
        return new ResponseEntity<>(createdCompany,HttpStatus.CREATED);
    }

    @PutMapping("/updateCompany/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long id,@Valid @RequestBody CompanyDto companyDto)
    {
        CompanyDto updateCompany = companyService.updateCompany(id,companyDto);
        return new ResponseEntity<>(updateCompany,HttpStatus.OK);
    }

    @DeleteMapping("/deleteCompany/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id){
        companyService.DeleteCompany(id);
        return new ResponseEntity<>("Company Deleted",HttpStatus.OK);
    }
}
