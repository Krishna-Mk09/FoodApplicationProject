package com.userauthentication.jdp.repository;

import com.userauthentication.jdp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
 * Author Name : M.V.Krishna
 * Date: 27-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);
    Optional<User> findByEmail(String email);
    User findByPhoneNum(String phoneNum);
    User findByUserNameAndPassword(String userName, String password);
    User findByEmailAndPassword(String email, String password);
    void deleteByUserId(long userId);
    boolean existsByEmailOrPhoneNum(String email, String phoneNum);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.userId = :userId")
    void updateUserPassword(Long userId, String password);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.userId = :userId")
    void updateUserRole(long userId, String role);


}
