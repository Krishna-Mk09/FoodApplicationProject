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
@Table(name = "restaurant_licences")
public class RestaurantLicence {

    @Id
    @Column(name = "licence_id")
    private long id;

    @Column(name = "restaurant_id")
    private long restaurantId;

    @Column(name = "licence_type", nullable = false, length = 100)
    private String licenceType;

    @Column(name = "licence_name", nullable = false, length = 100)
    private String licenceName;

    @Column(name = "licence_number", nullable = false, length = 100)
    private String licenceNumber;

    @Column(name = "licence_issue_date")
    private LocalDate licenceIssueDate;

    @Column(name = "licence_expiry_date")
    private LocalDate licenceExpiryDate;



    @Column(name = "licence_description", length = 500)
    private String licenceDescription;

    @Column(name = "licence_authority", length = 500)
    private String licenceAuthority;

    @Column(name = "licence_document_status", length = 50)
    private String licenceDocumentStatus;

    @Column(name = "licence_document_issued_by")
    private String licenceDocumentIssuedBy;

    @Column(name = "user_id")
    private long userId;
}