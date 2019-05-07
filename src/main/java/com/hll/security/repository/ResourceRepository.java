package com.hll.security.repository;

import com.hll.security.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {

    Resource findByUrl(String url);
}
