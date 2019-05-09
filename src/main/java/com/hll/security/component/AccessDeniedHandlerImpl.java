package com.hll.security.component;

import com.alibaba.fastjson.JSON;
import com.hll.security.utils.JsonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义403响应内容
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        //setContentType和setCharacterEncoding都需要设置下编码，不然页面提示文字会乱码
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        JsonResult result = new JsonResult();
        result.setCode(403);
        result.setMessage("权限不足，请联系管理员");
        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
    }
}
