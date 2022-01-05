package ru.zim.ates.common.application.config.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;


public class AuthHeaderHandlingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        Cookie cookie = WebUtils.getCookie(request, "access_token");
        if (cookie != null) {
            String tokenValue = cookie.getValue();
            req = new AuthHeaderHandlingRequestWrapper(request, tokenValue);
        }
        chain.doFilter(req, res);
    }

}
