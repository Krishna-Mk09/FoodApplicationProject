package com.foodapplication.jdp.Common_Service.Entity;

/*
 * Author Name : M.V.Krishna
 * Date: 11-05-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDTO {
    private Long userId;
    private String email;
    private String phoneNum;
    private String userName;
    private String firstName;
    private String lastName;
    private LocalDateTime expDate;
    private String secondaryEmail;
    private String createdBy;
    private LocalDateTime creationDate;
    private BigDecimal imageSize;
    private String role;
    private String businessEmail;
    private String gstNumber;
    private String panNumber;
    private Long aadhaarNumber;
    private String governmentIssuedId;
    private String nameAsInLicense;
    private String licenseNumber;
    private Integer noOfLoginAttempts;
    private Boolean isActive;
    private List<AddressDTO> addresses;

}
