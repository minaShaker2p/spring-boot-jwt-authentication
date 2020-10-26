package com.mina.springbootjwtauthentication.repository;


import com.mina.springbootjwtauthentication.model.ERole;
import com.mina.springbootjwtauthentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole role);
}
