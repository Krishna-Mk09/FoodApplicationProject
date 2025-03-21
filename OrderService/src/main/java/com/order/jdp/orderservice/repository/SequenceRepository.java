package com.order.jdp.orderservice.repository;

import com.order.jdp.orderservice.entity.FOODSequences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SequenceRepository  extends JpaRepository<FOODSequences, String> {
    List<FOODSequences> findByTableName(String tableName);
}
