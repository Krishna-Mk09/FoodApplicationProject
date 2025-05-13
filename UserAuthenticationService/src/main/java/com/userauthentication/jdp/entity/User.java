package com.userauthentication.jdp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/*
 * Author Name : M.V.Krishna
 * Date: 26-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "USERS")
@JsonIgnoreProperties({"attribute_1,attribute_2,attribute_3,attribute_4,attribute_5", "addresses"})
public class User {
    @Id
    @Column(name = "USER_ID", nullable = false)
    @JsonProperty("user_id")
    private long userId;
    @NotNull
    @Column(name = "EMAIL", unique = true)
    private String email;
    @NotNull
    @JsonProperty("phone_num")
    @Column(name = "PHONE_NUM", unique = true)
    private String phoneNum;
    @NotNull
    @Column(name = "USER_NAME")
    @JsonProperty("user_name")
    private String userName;
    @NotNull
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "EXP_DATE")
    private LocalDateTime expDate;
    @Column(name = "IMAGE")
    private byte[] image;
    @Column(name = "SECONDARY_EMAIL")
    private String secondaryEmail;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @Column(name = "IMAGE_SIZE")
    private BigDecimal imageSize;
    @Column(name = "ROLE")
    private String role;
    @Column(name = "BUSINESS_EMAIL")
    private String businessEmail;
    @Column(name = "GST_NUMBER")
    private String gstNumber;
    @Column(name = "PAN_NUMBER")
    private String panNumber;
    @Column(name = "AADHAR_NUMBER")
    private Long aadhaarNumber;
    @Column(name = "GOVERNMENT_ISSUED_ID")
    private String government_issued_id;
    @Column(name = "NAME_ASIN_LICENSE")
    private String nameAsInLicense;
    @Column(name = "LICENSE_NUMBER")
    private String licenseNumber;
    @Column(name = "PROFILE_PHOTO")
    private byte[] profilePhoto;
    @Column(name = "RESTAURANT_PHOTO")
    private byte[] restaurantPhoto;
    @Column(name = "NO_OF_LOGIN_ATTEMPTS")
    private Integer noOfLoginAttempts;
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> address;

}
