package com.userauthentication.jdp.beans;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.userauthentication.jdp.entity.User;
import org.springframework.stereotype.Component;

/*
 * Author Name : M.V.Krishna
 * Date: 11-05-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setPhoneNum(user.getPhoneNum());
        dto.setUserName(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setExpDate(user.getExpDate());
        dto.setSecondaryEmail(user.getSecondaryEmail());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setCreationDate(user.getCreationDate());
        dto.setImageSize(user.getImageSize());
        dto.setRole(user.getRole());
        dto.setBusinessEmail(user.getBusinessEmail());
        dto.setGstNumber(user.getGstNumber());
        dto.setPanNumber(user.getPanNumber());
        dto.setAadhaarNumber(user.getAadhaarNumber());
        dto.setGovernmentIssuedId(user.getGovernment_issued_id());
        dto.setNameAsInLicense(user.getNameAsInLicense());
        dto.setLicenseNumber(user.getLicenseNumber());
        dto.setNoOfLoginAttempts(user.getNoOfLoginAttempts());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}

