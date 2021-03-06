package com.hll.security.controller;

import com.hll.security.config.UserDetailsImpl;
import com.hll.security.entity.Resource;
import com.hll.security.repository.ResourceRepository;
import com.hll.security.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        //成功登陆后，根据当前用户角色查询对应的菜单
        //Principal principal = request.getUserPrincipal();
        //System.out.println(principal.getName());

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator it = authorities.iterator();

        //一级菜单
        List<Resource> listOnes = new ArrayList<>();
        //二级菜单
        List<Resource> listTwos = new ArrayList<>();

        if(it.hasNext()) {
            String roleName = it.next().toString();
            if (redisTemplate.hasKey(roleName + "_menu_one")) {
                System.out.println("从redis获取菜单1");
                listOnes =(List<Resource>) redisTemplate.opsForValue().get(roleName+"_menu_one");
            } else {
                //System.out.println(it.next());
                List<Resource> resources = resourceRepository.getResourceByRoleName(roleName);
                for (Resource r : resources) {
                    if (r.getLevel() == 1) {
                        listOnes.add(r);
                    }
                }
                System.out.println("从数据库获取菜单1");
                //要改用opsForList的方式存储，不然redis里面存的数据看不懂
                redisTemplate.opsForValue().set(roleName+"_menu_one", listOnes);
            }

            if (redisTemplate.hasKey(roleName + "_menu_two")) {
                System.out.println("从redis获取菜单2");
                listTwos = (List<Resource>) redisTemplate.opsForValue().get(roleName + "_menu_two");
            } else {
                List<Resource> res = resourceRepository.findByParentId(listOnes.get(0).getId());
                //List<Resource> res = resourceRepository.findAll();
                listTwos.addAll(res);

                redisTemplate.opsForValue().set(roleName+"_menu_two", listTwos);
                System.out.println("从数据库获取菜单2");
            }
        }



        model.addAttribute("listOnes", listOnes);
        model.addAttribute("listTwos", listTwos);
        return "home";
    }

    @PostMapping("loadLeftMenu")
    @ResponseBody
    public JsonResult loadLeftMenu(String id, Model model) {
        List<Resource> resources = resourceRepository.findByParentId(id);

        JsonResult result = new JsonResult();
        result.setFlag(true);
        result.setMessage("加载左侧菜单成功");
        result.setData(resources);

        return result;
    }
}
