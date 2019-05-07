package com.hll.security.service;

import com.hll.security.entity.Resource;
import com.hll.security.entity.Role;
import com.hll.security.repository.ResourceRepository;
import com.hll.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RoleRepository roleRepository;

    public Resource findResouceByUrl(String url) {
        return resourceRepository.findByUrl(url);
    }

    public List<Role> getRoles(String resouceId) {
        return roleRepository.findRoleByResouceId(resouceId);
    }
}
