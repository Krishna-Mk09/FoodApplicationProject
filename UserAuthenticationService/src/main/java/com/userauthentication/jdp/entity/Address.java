package com.userauthentication.jdp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

/*
 * Author Name : M.V.Krishna
 * Date: 26-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ADDRESSES")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID", updatable = false, nullable = false)
    private Long addressId;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
    @Column(name = "ADDRESS_LINE1", nullable = false, length = 100)
    private String addressLine1;

    @Column(name = "ADDRESS_LINE2", length = 100)
    private String addressLine2;

    @Column(name = "CITY", nullable = false, length = 50)
    private String city;

    @Column(name = "STATE", nullable = false, length = 50)
    private String state;

    @Column(name = "COUNTRY", nullable = false, length = 50)
    private String country;

    @Column(name = "ZIP_CODE", nullable = false, length = 5)
    private String zipCode;

    @Column(name = "ADDRESS_TYPE", nullable = false, length = 20)
    private String addressType;
}
