package com.hll.security.component;


import com.hll.security.entity.Resource;
import com.hll.security.entity.Role;
import com.hll.security.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.security.Security;
import java.util.Collection;
import java.util.List;

/**
 * 接收用户请求的地址，返回访问该地址的所有权限
 * @author hll
 * @date 2019/5/5 22:06
 */
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private ResourceService resourceService;

    //接收用户请求的地址，返回访问该地址的所有权限
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String url = ((FilterInvocation) o).getRequestUrl();
        System.out.println("请求地址：" + url);

        //登陆页不需要权限
        if ("/login".equals(url)) {
            return null;
        }

        Resource resource = resourceService.findResouceByUrl(url);
        //没有匹配的url，说明都可以访问
        if (resource == null) {
            return SecurityConfig.createList("ROLE_LOGIN");
        }

        //将resource所需要的roles按照框架的要求封装返回
        List<Role> roles = resourceService.getRoles(resource.getId());
        int size = roles.size();
        String[] values = new String[size];
        for (int i=0; i<size; i++) {
            values[i] = roles.get(i).getRoleName();
        }

        return SecurityConfig.createList(values);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
