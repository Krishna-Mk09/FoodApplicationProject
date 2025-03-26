package com.foodapplication.jdp.Common_Service.repository;

import com.foodapplication.jdp.Common_Service.Entity.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Author Name : M.V.Krishna
 * Date: 25-03-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Repository
public interface SequenceRepository extends JpaRepository<Sequence,String> {

    List<Sequence> findByTableName(String tableName);

}
