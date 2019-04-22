package com.business.exchange.config;

import com.alibaba.fastjson.JSON;
import com.business.exchange.constant.RespDefine;
import com.business.exchange.model.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 域名的session设置
 */
@Configuration
public class SessionCofig implements WebMvcConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionCofig.class);
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor())
                //排除拦截
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/create")
                .excludePathPatterns("/user/logout")
                .excludePathPatterns("/user/import_accounts")
//                .excludePathPatterns("/exchange/export_bill")

                //拦截路径
                .addPathPatterns("/**");
    }

    @Configuration
    public class SecurityInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            /*if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
                response.setStatus(200);
                return true;
            }*/

            HttpSession session = request.getSession();
            LOGGER.info("session id : {}.", session.getId());
            LOGGER.info("session attr: {}.", session.getAttribute(session.getId()));
            if (session.getAttribute(session.getId()) != null){
                return true;
            }
            response.getWriter().write(JSON.toJSONString(
                    new BaseResponse(RespDefine.CODE_NEED_LOGIN, RespDefine.DESC_NEED_LOGIN)
            ));
            return false;
        }
    }
}
