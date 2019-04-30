package com.hll.security.repository;

import com.hll.security.entity.Role;
import com.hll.security.entity.UserRole;
import com.hll.security.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    List<Role> findByUser(Users user);
}
