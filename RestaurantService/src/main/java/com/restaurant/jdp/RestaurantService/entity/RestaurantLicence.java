package com.restaurant.jdp.RestaurantService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;

    @Column(name = "LICENCE_TYPE", nullable = false, length = 100)
    private String licenceType;

    @Column(name = "LICENCE_NAME", nullable = false, length = 100)
    private String licenceName;

    @Column(name = "LICENCE_NUMBER", nullable = false, length = 100)
    private String licenceNumber;

    @Column(name = "LICENCE_ISSUE_DATE")
    private LocalDate licenceIssueDate;

    @Column(name = "LICENCE_EXPIRY_DATE")
    private LocalDate licenceExpiryDate;

    @Column(name = "LICENCE_DESCRIPTION", length = 500)
    private String licenceDescription;

    @Column(name = "LICENCE_AUTHORITY", length = 500)
    private String licenceAuthority;

    @Column(name = "LICENCE_DOCUMENT_STATUS", length = 50)
    private String licenceDocumentStatus;

    @Column(name = "LICENCE_DOCUMENT_ISSUED_BY")
    private String licenceDocumentIssuedBy;

    @Column(name = "USER_ID")
    private long userId;
}
