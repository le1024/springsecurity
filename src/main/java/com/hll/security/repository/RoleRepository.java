package com.hll.security.repository;

import com.hll.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    @Query(value = "slect r.* from role r left join role_resouce rs on rs.role_id = r.id where rs.resouce_id = ?1", nativeQuery = true)
    List<Role> findRoleByResouceId(String resouceId);
}
