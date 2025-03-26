package com.foodapplication.jdp.Common_Service.Service;

import org.springframework.stereotype.Repository;

/*
 * Author Name : M.V.Krishna
 * Date: 25-03-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
public interface SequenceService {
    long getSequenceByCustomer(String tableName, long userId) throws Exception;
}
