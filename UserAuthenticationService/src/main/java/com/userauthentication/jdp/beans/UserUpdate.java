package com.userauthentication.jdp.beans;

/*
 * Author Name : M.V.Krishna
 * Date: 10-05-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Setter
@Getter
public class UserUpdate {
    String userName;
    String userBusinessEmail;
    String panNumber;
    Long aadhaarNumber;
    byte[] restaurantPhoto;
    String nameAsInLicense;
    String licenseNumber;
    String gstNumber;
}
