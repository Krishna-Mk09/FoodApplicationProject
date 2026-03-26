package com.foodapplication.jdp.Common_Service.Entity;

import lombok.*;

/*
 * Author Name : M.V.Krishna
 * Date: 11-05-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDTO {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
