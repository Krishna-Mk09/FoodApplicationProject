package com.userauthentication.jdp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Author Name : M.V.Krishna
 * Date: 26-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ADDRESS")
public class Address {
    @Id
    @Column(name = "ADDRESS_ID", nullable = false)
    @JsonProperty("address_id")
    private long addressId;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonProperty("user_id")
    private User user;
    @NotNull
    @Column(name = "ADDRESS_LINE1", nullable = false, length = 100)
    private String addressLine1;

    @Column(name = "ADDRESS_LINE2", length = 100)
    private String addressLine2;

    @NotNull
    @Column(name = "CITY", nullable = false, length = 50)
    private String city;

    @NotNull
    @Column(name = "STATE", nullable = false, length = 50)
    private String state;

    @NotNull
    @Column(name = "COUNTRY", nullable = false, length = 50)
    private String country;
    @NotNull
    @Column(name = "ZIP_CODE", nullable = false, length = 5)
    private String zipCode;

    @NotNull
    @Column(name = "ADDRESS_TYPE", nullable = false, length = 20)
    private String addressType;
}
