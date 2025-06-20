package com.idealzouhu.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.idealzouhu.reggie.common.BaseContext;
import com.idealzouhu.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成了登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 路径匹配，检测本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI){
        for( String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的URI
        String requestURI = request.getRequestURI();
//        log.info("拦截到请求： {}",request.getRequestURI());

        // 2. 判断本次请求是否需要处理
        String[] urls = new String[]{        // 定义不需要处理的请求路径;
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",  // 移动端发送短信
                "/user/login"     // 移动端登录
        };
        boolean check = check(urls, requestURI);

        // 3. 如果不需要处理，则直接放行
        if(check){
//            log.info("本次请求  {} 不需要处理",request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 判断后端用户登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            // log.info("用户已登录，id为: {} ",request.getSession().getAttribute("employee"));
            // log.info("线程id: {}" , Thread.currentThread().getId()) ;
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));  // 保存用户id到ThreadLocal里
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 判断前端用户登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            // log.info("用户已登录，id为: {} ",request.getSession().getAttribute("user"));
            // log.info("线程id: {}" , Thread.currentThread().getId()) ;
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("user"));  // 保存用户id到ThreadLocal里
            filterChain.doFilter(request, response);
            return;
        }

        // 6. 如果未登录则返回未登录结果. 通过输出流方式向客户端页面响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
}
