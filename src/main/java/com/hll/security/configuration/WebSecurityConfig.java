package com.hll.security.configuration;

import com.hll.security.component.AccessDecisionManagerImpl;
import com.hll.security.component.AccessDeniedHandlerImpl;
import com.hll.security.component.FilterInvocationSecurityMetadataSourceImpl;
import com.hll.security.service.UsersService;
import com.hll.security.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author hll
 * @date 2019/5/5 22:29
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsersService usersService;

    //根据一个url请求，获得访问它所需要的roles权限
    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    //接收一个用户的信息和访问一个url所需要的权限，判断该用户是否可以访问
    @Autowired
    private AccessDecisionManagerImpl accessDecisionManager;

    //403
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    /**
     * 定义认证用户信息获取来源，密码校验规则等
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       //通过注入userDetailsService的方式实现
        auth.userDetailsService(usersService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 配置不需要认证的页面
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", "/noAuthenicate");
    }

    /**
     * 定义安全策略
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
                        o.setAccessDecisionManager(accessDecisionManager);
                        return o;
                    }
                })
                .and()
                .formLogin()
                .loginPage("/login") //请求controller "/login"访问自定义页面
                .loginProcessingUrl("/user/login") //页面请求的登陆接口，比如：Ajax的url
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                   // @Override
                   // public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();

                        JsonResult result = new JsonResult();
                        result.setMessage("访问成功");
                        result.setFlag(true);
                        out.write(result.toString());
                        out.flush();
                        out.close();
                   // }
                })
                .failureHandler((request, response, e) -> {
                    //@Override
                    //public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        response.setContentType("application/json;chartset=utf-8");
                        PrintWriter out = response.getWriter();

                        JsonResult result = new JsonResult();
                        result.setFlag(false);
                        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
                            result.setMessage("用户名或者密码输入错误,登陆失败");
                        } else if (e instanceof DisabledException) {
                            result.setMessage("账户被禁用，请联系管理员");
                        } else if (e instanceof LockedException) {
                            result.setMessage("账户被锁定，请联系管理员");
                        } else {
                            result.setMessage("登陆失败");
                        }

                        out.write(result.toString());
                        out.flush();
                        out.close();
                    //}
                })
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }
}
