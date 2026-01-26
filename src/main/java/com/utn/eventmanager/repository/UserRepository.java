package com.utn.eventmanager.repository;

import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //-------------------------------------------------//
    //----------- SEARCH FOR USER BY EMAIL -----------//
    //-----------------------------------------------//
    Optional<User> findByEmail(String email);

    //--------------------------------------------------//
    //----------- VALIDATE DUPLICATE EMAILS -----------//
    //------------------------------------------------//
    boolean existsByEmailIgnoreCase(String email);

    //-----------------------------------------------//
    //----------- LIST USERS BY ROLE ---------------//
    //---------------------------------------------//
    List<User> findByRole(UserRole role);

    //-------------------------------------------------//
    //----------- LIST ONLY ACTIVATE USERS -----------//
    //-----------------------------------------------//
    List<User> findByActiveTrue();

    //--------------------------------------------------------//
    //----------- SEARCH FOR ACTIVE USER BY EMAIL -----------//
    //------------------------------------------------------//
    Optional<User> findByEmailAndActiveTrue(String email);

    //--------------------------------------------------------------//
    //----------- VALIDATE DUPLICATES BY RETURNING USER -----------//
    //------------------------------------------------------------//
    Optional<User> findByEmailIgnoreCase(String email);
}
