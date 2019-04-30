package com.hll.security.service;

import com.hll.security.config.UserDetailsImpl;
import com.hll.security.entity.Users;
import com.hll.security.repository.RoleRepository;
import com.hll.security.repository.UserRoleRepository;
import com.hll.security.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 实现UserDetailsService接口类
 * 加载用户信息，通过loadUserByUsername返回一个UserDetails封装的user对象
 */
@Service
public class UsersService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("加载用户:" + username);

        Users user = getByUsername(username).get(0);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new UserDetailsImpl(user, userRoleRepository.findByUser(user));
    }

    @Transactional
    public List<Users> getAllUser() {
        return usersRepository.findAll();
    }

    @Transactional
    public List<Users> getByUsername(String username) {
        return usersRepository.findByUsername(username);
    }
}
