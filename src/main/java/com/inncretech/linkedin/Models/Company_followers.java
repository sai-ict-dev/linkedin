package com.inncretech.linkedin.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company_followers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"companyId", "followerId"}))
public class Company_followers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "companyId", referencedColumnName = "Id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "followerId", referencedColumnName = "Id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "Id")
    private Roles role;

}
