/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.api.swagger;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SwaggerInterceptAdapter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String req = request.getRequestURI();

        if ((req == null) || req.isEmpty() || req.equals("/") || req.equals("/v1") || req
                .equals("/v1/")) {
            response.sendRedirect("/swagger-ui.html");
            return false;
        }

        return super.preHandle(request, response, handler);
    }

}
