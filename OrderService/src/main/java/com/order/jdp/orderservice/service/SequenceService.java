package com.order.jdp.orderservice.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface SequenceService {

    public long getSequenceByCustomer(String tableName, long userId) throws Exception;

}
