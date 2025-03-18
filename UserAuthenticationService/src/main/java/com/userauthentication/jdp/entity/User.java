package com.userauthentication.jdp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

@Data // Lombok will generate getters and setters for all fields
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "USERS")
@JsonIgnoreProperties({"orders", "addresses"}) // Prevents infinite loops in JSON responses
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", updatable = false, nullable = false)
    private Long userId;

    @Column(name = "USER_NAME", unique = true, nullable = false, length = 50)
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EXP_DATE")
    private LocalDateTime expDate;

    @Column(name = "EMAIL")
    private String email;  // Ensure this is declared

    @Column(name = "PHONE_NUM", length = 15)
    private String phoneNum;

    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "SECONDARY_EMAIL")
    private String secondaryEmail;

    @Column(name = "CREATED_BY")
    private BigDecimal createdBy;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "IMAGE_SIZE")
    private BigDecimal imageSize;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "NO_OF_LOGIN_ATTEMPTS")
    private Integer noOfLoginAttempts;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> address;

    // Optional: Uncomment the following if you are using the Order entity
    // @JsonManagedReference
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Order> orders;


}
