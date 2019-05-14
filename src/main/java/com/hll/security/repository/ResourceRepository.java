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

    //@Query("select rr.resource from RoleResource rr left join Role r where r.roleName = ?1 ")
    @Query(value = "select res.* from resource res " +
            "left join role_resource rr on rr.resource_id = res.id " +
            "left join role role on role.id = rr.role_id " +
            "where role.role_name = ?1 ", nativeQuery = true)
    List<Resource> getResourceByRoleName(String roleName);

    List<Resource> findByParentId(String id);
}
