package com.hll.security.repository;

import com.hll.security.entity.Role;
import com.hll.security.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    @Query(value = "select r.* from role r left join role_resource rs on rs.role_id = r.id where rs.resource_id = ?1", nativeQuery = true)
    List<Role> findRoleByResourceId(String resourceId);

    @Query("select ur.role from UserRole ur left join ur.role r where ur.user = ?1")
    List<Role> findRoleByUser(Users user);
}
