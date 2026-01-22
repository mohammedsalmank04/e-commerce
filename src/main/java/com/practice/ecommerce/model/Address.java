package com.practice.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AddressId;
    @NotBlank
    @Size(min = 5, message = "Street Size must be atleast 5 character")
    private String street;
    @NotBlank
    @Size(min = 5, message = "building name must be atleast 5 character")
    private String buildingName;
    @NotBlank
    @Size(min = 3, message = "city name must be atleast 3 character")
    private String city;
    @NotBlank
    @Size(min = 2, message = "State name must be atleast 2 character")
    private String state;

    @NotBlank
    @Size(min = 2, message = "country name must be atleast 2 character")
    private String country;

    @NotBlank
    @Size(min = 5, message = "ZipCode name must be atleast 3 character")
    private String zipCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String zipCode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }
}
