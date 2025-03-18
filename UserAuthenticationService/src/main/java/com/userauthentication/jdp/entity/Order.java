//package com.userauthentication.jdp.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
///*
// * Author Name : M.V.Krishna
// * Date: 26-02-2025
// * Created With: IntelliJ IDEA Ultimate Edition
// */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "ORDERS")
//public class Order {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ORDER_ID", updatable = false, nullable = false)
//    private Long orderId;
//
//    @ManyToOne
//    @JsonBackReference
//    @JoinColumn(name = "USER_ID", nullable = false)
//    private User user;
//    @ManyToOne
//    @JoinColumn(name = "ADDRESS_ID") // Order is linked to an Address
//    private Address shippingAddress;
//
//    @Column(name = "ORDER_DATE", nullable = false)
//    private LocalDateTime orderDate;
//
//    @Column(name = "ORDER_STATUS", nullable = false, length = 20)
//    private String orderStatus;
//
//    @Column(name = "ORDER_AMOUNT", nullable = false, precision = 10, scale = 2)
//    private BigDecimal orderAmount;
//
//    @Column(name = "ORDER_DESCRIPTION", length = 255)
//    private String orderDescription;
//
//    @Column(name = "ORDER_TYPE", length = 50)
//    private String orderType;
//
//    @Column(name = "ORDER_QUANTITY", nullable = false)
//    private Integer orderQuantity;
//
//
////    private String orderPaymentType;
////    private String orderPaymentStatus;
////    private String orderPaymentDate;
////    private String orderPaymentAmount;
////    private String orderPaymentDescription;
////    private String orderPaymentId;
////    private String orderPaymentMode;
////    private String orderPaymentRefNum;
////    private String orderPaymentBankName;
////    private String orderPaymentBankBranch;
////    private String orderPaymentBankCity;
////    private String orderPaymentBankState;
////    private String orderPaymentBankCountry;
////    private String orderPaymentBankZipCode;
////    private String orderPaymentBankPhoneNum;
////    private String orderPaymentBankEmail;
////    private String orderPaymentBankAddress;
////    private String orderPaymentBankAccountNum;
////    private String orderPaymentBankAccountName;
////    private String orderPaymentBankIFSCCode;
////    private String orderPaymentBankMICRCode;
////    private String orderPaymentBankBranchCode;
////    private String orderPaymentBankSWIFTCode;
////    private String orderPaymentBankRoutingNum;
////    private String orderPaymentBankSortCode;
////    private String orderPaymentBankBSBCode;
////    private String orderPaymentBankIBANCode;
////    private String orderPaymentBankBICCode;
////    private String orderPaymentBankABACode;
////    private String orderPaymentBankEFTCode;
////    private String orderPaymentBankCHIPSCode;
////    private String orderPaymentBankNCCCode;
////    private String orderPaymentBankBSBNum;
////    private String orderPaymentBankCLABECode;
////    private String orderPaymentBankCCCode;
////    private String orderPaymentBankCSCCode;
////    private String orderPaymentBankCVVCode;
////    private String orderPaymentBankPINCode;
////    private String orderPaymentBankOTPCode;
////    private String orderPaymentBankTANCode;
////    private String orderPaymentBankHINCode;
////    private String orderPaymentBankTINCode;
////    private String orderPaymentBankVATCode;
////    private String orderPaymentBankGSTCode;
////    private String orderPaymentBankPANCode;
////    private String orderPaymentBankC;
//}
