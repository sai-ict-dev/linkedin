package com.inncretech.linkedin.Services;

import com.inncretech.linkedin.Models.*;
import com.inncretech.linkedin.DTOs.*;
import com.inncretech.linkedin.Repository.*;
import com.inncretech.linkedin.Mappers.*;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyService companyService;

    private Long companyId;

    private Company company;

    private CompanyDto companyDto;

    @BeforeMethod
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        companyId = 1L;
        company = new Company();
        company.setId(companyId);
        company.setName("random company");
        company.setProfile("random profile");

    }
    @DataProvider(name = "companyDtoValidData")
    public Object[] companyDtoValidData()
    {
        companyDto = new CompanyDto();
        companyDto.setName("random company");
        companyDto.setProfile("random profile");

        CompanyDto dto2 = new CompanyDto();
        dto2.setProfile("test profile 2");
        dto2.setName("test company name 2");

        return new Object[]{companyDto,dto2};
    }

    @Test(dataProvider = "companyDtoValidData")
    public void testCreateCompanyValidData(CompanyDto companyDto)
    {
        when(companyRepository.findByName(companyDto.getName())).thenReturn(Optional.empty());
        when(companyMapper.companyDtoToCompany(companyDto)).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.companyToCompanyDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.createCompany(companyDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,companyDto);
    }

    @Test(dataProvider = "companyDtoValidData")
    public void testCreateExistingCompany(CompanyDto companyDto){
        when(companyRepository.findByName(companyDto.getName())).thenReturn(Optional.of(company));

        try{
            companyService.createCompany(companyDto);
        }
        catch (Exception  ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company with " + companyDto.getName() + " already exists!");
        }
    }

    @Test
    public void testCreateCompanyDeletedCompany()
    {
        companyDto = new CompanyDto();
        companyDto.setName("random company");
        companyDto.setProfile("random profile");

        company.setDeleted(true);
        when(companyRepository.findByName(companyDto.getName())).thenReturn(Optional.of(company));
        when(companyMapper.companyDtoToCompany(companyDto)).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.companyToCompanyDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.createCompany(companyDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,companyDto);
    }

    @Test
    public void testGetCompanyByIdValidId()
    {
        companyDto = new CompanyDto();
        companyDto.setName("random company");
        companyDto.setProfile("random profile");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(companyMapper.companyToCompanyDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.getCompanyById(companyId);

        Assert.assertNotNull(result);
        Assert.assertEquals(result,companyDto);
    }
    @Test
    public void testGetCompanyIdInvalidId()
    {
        companyId = 99999L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        try{
            companyService.getCompanyById(companyId);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company Not found");
        }
    }

    @Test
    public void testGetCompanyIdDeletedId()
    {
        company.setDeleted(true);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        try{
            companyService.getCompanyById(companyId);
        }
        catch (IllegalArgumentException ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company Not found");
        }
    }


    @Test
    public void testGetAllCompaniesAllValid()
    {
        Company c1 = new Company();
        c1.setDeleted(false);

        Company c2 = new Company();
        c2.setDeleted(false);

        Company c3 = new Company();
        c3.setDeleted(false);

        List<Company> companies = Arrays.asList(c1,c2,c3);
        CompanyDto dto1 = new CompanyDto();
        CompanyDto dto2 = new CompanyDto();
        CompanyDto dto3 = new CompanyDto();

        when(companyRepository.findAll()).thenReturn(companies);
        when(companyMapper.companyToCompanyDto(c1)).thenReturn(dto1);
        when(companyMapper.companyToCompanyDto(c2)).thenReturn(dto2);
        when(companyMapper.companyToCompanyDto(c3)).thenReturn(dto3);

        List<CompanyDto> result = companyService.getAllCompanies();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains(dto1));
        Assert.assertTrue(result.contains(dto2));
        Assert.assertTrue(result.contains(dto3));
    }

    @Test
    public void testGetAllCompaniesAllDeleted()
    {
        Company c1 = new Company();
        c1.setDeleted(true);

        Company c2 = new Company();
        c2.setDeleted(true);

        Company c3 = new Company();
        c3.setDeleted(true);
        List<Company> companies = new ArrayList<>();
        when(companyRepository.findAll()).thenReturn(companies);

        List<CompanyDto> result = companyService.getAllCompanies();
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateCompanyValidData()
    {
        companyDto = new CompanyDto();
        companyDto.setName("updated company name");
        companyDto.setProfile("updated profile");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.companyToCompanyDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.updateCompany(companyId,companyDto);

        Assert.assertNotNull(result);
        Assert.assertEquals(company.getName(),"updated company name");
        Assert.assertEquals(company.getProfile(),"updated profile");
    }

    @Test
    public void testUpdateCompanyInvalidId()
    {
        companyId = 9999L;
        companyDto = new CompanyDto();
        companyDto.setName("updated company name");
        companyDto.setProfile("updated profile");

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());
        try{
            companyService.updateCompany(companyId,companyDto);
        }
        catch (Exception ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company Not Found");
        }
    }

    @Test
    public void testUpdateCompanyDeletedId()
    {
        company.setDeleted(true);
        companyDto = new CompanyDto();
        companyDto.setName("updated company name");
        companyDto.setProfile("updated profile");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        try{
            companyService.updateCompany(companyId,companyDto);
        }
        catch (Exception ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company Not Found");
        }
    }

    @Test
    public void testDeleteCompanyValidId()
    {
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(companyRepository.save(company)).thenReturn(company);

        companyService.DeleteCompany(companyId);

        Assert.assertTrue(company.isDeleted());
    }

    @Test
    public void testDeleteCompanyInvalidId()
    {
        companyId = 99999L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        try{
            companyService.DeleteCompany(companyId);
        }
        catch (Exception ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company Not Found");
        }
    }


    @Test
    public void testDeletePostDeletedId()
    {
        company.setDeleted(true);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        try{
            companyService.DeleteCompany(companyId);
        }
        catch (Exception ex)
        {
            Assert.assertEquals(ex.getMessage(),"Company Not Found");
        }
    }
}
