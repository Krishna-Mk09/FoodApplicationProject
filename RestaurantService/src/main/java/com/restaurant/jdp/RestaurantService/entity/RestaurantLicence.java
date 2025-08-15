package com.restaurant.jdp.RestaurantService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "RESTAURANT_LICENCES")
public class RestaurantLicence {

    @Id
    @Column(name = "LICENCE_ID")
    private Long licenceId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;

    @Column(name = "LICENCE_NAME", nullable = false, length = 100)
    private String licenceName;

    @Column(name = "NameAsInLicense", nullable = false, length = 100)
    private String nameAsInLicense;

    @Column(name = "LICENCE_NUMBER", nullable = false, length = 100)
    private String licenceNumber;


}
