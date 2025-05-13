package com.foodapplication.jdp.Common_Service.Service;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;

/*
 * Author Name : M.V.Krishna
 * Date: 25-03-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
public interface SequenceService {
    long getSequenceByCustomer(String tableName) throws Exception;

    UserDTO getCurrentUser(String token) throws Exception;
}
