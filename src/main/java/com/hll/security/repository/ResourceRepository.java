package com.hll.security.repository;

import com.hll.security.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {

    Resource findByUrl(String url);

    @Query("select rr.resource from RoleResource rr where rr.role.id = ?1 ")
    List<Resource> getResourceByRoleId(String roleId);
}
