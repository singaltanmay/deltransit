/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.api.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SwaggerInterceptAdapter extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(SwaggerInterceptAdapter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String req = request.getRequestURI();

        //Logging the request
        logger.info("Received new request: " + request.getRequestURL());

        if ((req == null) || req.isEmpty() || req.equals("/") || req.equals("/v1") || req
                .equals("/v1/")) {
            response.sendRedirect("/swagger-ui.html");
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        int status = response.getStatus();
        //Logging the response status
        boolean isRequestSuccessful = HttpStatus.valueOf(status) == HttpStatus.OK;
        logger.info("Request " + (isRequestSuccessful ? "successful" : "failed") + ": " + request.getRequestURL());
    }
}
